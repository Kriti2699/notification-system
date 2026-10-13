package com.notification.smsworkers.service;


import com.notification.smsworkers.dto.NotificationEvent;
import com.notification.smsworkers.entity.SmsTemplate;
import com.notification.smsworkers.repository.SmsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class SmsService {

    @Autowired
    private final SmsRepository smsTemplateRepository;
    @Autowired
    private final RestTemplate restTemplate;



    @Value("${sms.provider.url}")
    private String smsProviderUrl;

    @Value("${sms.provider.api-key}")
    private String apiKey;

    @Value("${notification.api.url}")
    private String notificationApiUrl;

    public SmsService(
            SmsRepository smsTemplateRepository,
            RestTemplate restTemplate) {

        this.smsTemplateRepository = smsTemplateRepository;
        this.restTemplate = restTemplate;
    }

    public void sendSms(NotificationEvent event) {

        try {

            System.out.println(
                    "Processing SMS notification: "
                            + event.getNotificationId()
            );

            // 1. Get template from database
            SmsTemplate template =
                    smsTemplateRepository
                            .findByTemplateCode(event.getTemplateCode())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "SMS Template not found: "
                                                    + event.getTemplateCode()
                                    )
                            );

            // 2. Get message
            String message = template.getMessage();

            // 3. Replace placeholders
            message = populateTemplate(
                    message,
                    event.getData()
            );

            System.out.println(
                    "SMS Recipient: "
                            + event.getRecipient()
            );

            System.out.println(
                    "SMS Message: "
                            + message
            );

            // 4. Send SMS
            sendToProvider(
                    event.getRecipient(),
                    message
            );

            // 5. Update status
            updateStatus(
                    event.getNotificationId(),
                    "SENT"
            );

            System.out.println(
                    "SMS sent successfully"
            );

        } catch (Exception ex) {

            System.err.println(
                    "SMS sending failed: "
                            + ex.getMessage()
            );

            updateStatus(
                    event.getNotificationId(),
                    "FAILED"
            );
        }
    }

    private String populateTemplate(
            String template,
            Map<String, String> data) {

        if (data == null || data.isEmpty()) {
            return template;
        }

        for (Map.Entry<String, String> entry :
                data.entrySet()) {

            String placeholder =
                    "{{" + entry.getKey() + "}}";

            String value = entry.getValue();

            if (value != null) {

                template = template.replace(
                        placeholder,
                        value
                );
            }
        }

        return template;
    }

    private void sendToProvider(
            String phoneNumber,
            String message) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );

        MultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();

        body.add("phone", phoneNumber);
        body.add("message", message);
        body.add("key", apiKey);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        smsProviderUrl,
                        HttpMethod.POST,
                        request,
                        String.class
                );

        System.out.println(
                "Textbelt HTTP Status: "
                        + response.getStatusCode()
        );

        System.out.println(
                "Textbelt Response: "
                        + response.getBody()
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "SMS provider failed: "
                            + response.getStatusCode()
            );
        }

        if (response.getBody() == null ||
                !response.getBody().contains("\"success\":true")) {

            throw new RuntimeException(
                    "Textbelt rejected SMS: "
                            + response.getBody()
            );
        }
    }

    private void updateStatus(
            String notificationId,
            String status) {

        String url =
                notificationApiUrl
                        + "/notify/"
                        + notificationId
                        + "/status";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        String body = """
                {
                    "status": "%s"
                }
                """.formatted(status);

        HttpEntity<String> request =
                new HttpEntity<>(
                        body,
                        headers
                );

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );
    }
    public void populatesmsData(SmsTemplate smsTemplate) {
        try {

            String tempId = UUID.randomUUID().toString();
            SmsTemplate tempSmsTemplate = new SmsTemplate();
            tempSmsTemplate.setTempId(tempId);
            tempSmsTemplate.setTemplateCode(smsTemplate.getTemplateCode());
            tempSmsTemplate.setStatus(smsTemplate.getStatus());
            tempSmsTemplate.setMessage(smsTemplate.getMessage());
            tempSmsTemplate.setCreatedAt(LocalDateTime.now());

            smsTemplateRepository.save(tempSmsTemplate);

            log.info("SMS data saved successfully. ");
        } catch (Exception ex) {
            log.error("Failed to save SMS data", ex);
            throw ex;
        }
    }

}