package com.notification.emailworker.service;

import com.notification.emailworker.dto.NotificationEvent;
import com.notification.emailworker.dto.StatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;
    private final JavaMailSender mailSender;

    @Value("${notification.api.url}")
    private String notificationApiUrl;

    public void sendEmail(NotificationEvent event) {

        try {

            // Simulate email sending
            log.info("Sending Email to {}", event.getRecipient());
            SimpleMailMessage message=new SimpleMailMessage();

            mailSender.send(message);
            StatusUpdateRequest request =
                    new StatusUpdateRequest();

            request.setStatus("SENT");

            restTemplate.put(
                    notificationApiUrl
                            + "/notify/"
                            + event.getNotificationId()
                            + "/status",
                    request
            );

            log.info("Status updated to SENT");

        } catch (Exception ex) {

            try {

                StatusUpdateRequest request =
                        new StatusUpdateRequest();

                request.setStatus("FAILED");

                restTemplate.put(
                        notificationApiUrl
                                + "/notify/"
                                + event.getNotificationId()
                                + "/status",
                        request
                );

            } catch (Exception ignored) {
            }

            log.error("Email sending failed", ex);
        }
    }
}