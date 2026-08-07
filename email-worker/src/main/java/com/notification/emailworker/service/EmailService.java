package com.notification.emailworker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.emailworker.dto.NotificationEvent;
import com.notification.emailworker.dto.StatusUpdateRequest;
import com.notification.emailworker.entity.EmailTemplate;
import com.notification.emailworker.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;
    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;

    @Value("${notification.api.url}")
    private String notificationApiUrl;

    public void sendEmail(NotificationEvent event) {

        try {
            log.info("Looking up templateCode: [{}]", event.getTemplateCode());
            // Step 1: Get template from database
            EmailTemplate template = emailRepository.findByTemplateCode(event.getTemplateCode()).orElseThrow(() -> new RuntimeException("Template not found"));

            // Step 2: Read HTML file
            ClassPathResource resource = new ClassPathResource("templates/" + template.getHtml_file());


            String body = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Step 3: Extract specific values from data map
            Map<String, String> data = event.getData();

            String name = data.get("name");
            String password = data.get("password");
            String companyName = data.get("companyName");

            log.info("Parsed data -> name: {}, companyName: {}", name, companyName);
// avoid logging password in plaintext in production

// Step 4: Replace placeholders in the HTML body
//            body = body.replace("{{name}}", name != null ? name : "");
            body = body.replace("{{password}}", password != null ? password : "");
            body = body.replace("{{companyName}}", companyName != null ? companyName : "");

            // Step 3: Replace placeholders
            body = body.replace("{{name}}", event.getRecipient());
            body = body.replace("{{recipient}}", event.getRecipient());
            log.info("Sending Email to {}", event.getRecipient());


            // Step 4: Send HTML email
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(event.getRecipient());
            helper.setSubject(template.getSubject());
            helper.setText(body, true); // true = HTML

            mailSender.send(mimeMessage);

            // Step 5: Update status
            StatusUpdateRequest request = new StatusUpdateRequest();
            request.setStatus("SENT");

            restTemplate.put(notificationApiUrl + "/notify/" + event.getNotificationId() + "/status", request);

            log.info("Status updated to SENT");

        } catch (Exception ex) {

            try {
                StatusUpdateRequest request = new StatusUpdateRequest();
                request.setStatus("FAILED");

                restTemplate.put(notificationApiUrl + "/notify/" + event.getNotificationId() + "/status", request);
            } catch (Exception ignored) {
            }

            log.error("Email sending failed", ex);
        }
    }

    public void populateEmailData(EmailTemplate emailTemplate) {
        try {

            String tempId = UUID.randomUUID().toString();
            EmailTemplate tempEmailTemplate = new EmailTemplate();
            tempEmailTemplate.setTempId(tempId);
            tempEmailTemplate.setTemplateCode(emailTemplate.getTemplateCode());
            tempEmailTemplate.setStatus(emailTemplate.getStatus());
            tempEmailTemplate.setSubject(emailTemplate.getSubject());
            tempEmailTemplate.setHtml_file(emailTemplate.getHtml_file());
            tempEmailTemplate.setCreatedAt(LocalDateTime.now());

            emailRepository.save(tempEmailTemplate);

            log.info("Email data saved successfully. ");
        } catch (Exception ex) {
            log.error("Failed to save email data", ex);
            throw ex;
        }
    }
}