package com.notification.notificationapi.service;

import com.notification.notificationapi.dto.NotificationEvent;
import com.notification.notificationapi.dto.NotificationRequest;
import com.notification.notificationapi.dto.NotificationResponse;
import com.notification.notificationapi.entity.Notification;
import com.notification.notificationapi.producer.NotificationProducer;
import com.notification.notificationapi.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final List<String> VALID_CHANELS = Arrays.asList("email", "sms", "push");
    private final NotificationProducer notificationProducer;
    private final NotificationRepository notificationRepository;

    public NotificationResponse send(NotificationRequest request) {

        if (!VALID_CHANELS.contains(request.getChannel())) {
            return new NotificationResponse(null,
                    "FAILED",
                    "Invalid channel: " + request.getChannel()
                            + ". Valid: email, sms, push");
        }
        if (request.getRecipient() == null || request.getRecipient().equals("")) {
            return new NotificationResponse(null,
                    "FAILED",
                    "Invalid recipient: " + request.getRecipient());

        }


        String notificationId = UUID.randomUUID().toString();
        NotificationEvent notificationEvent = new NotificationEvent(
                notificationId,
                request.getChannel(),
                request.getRecipient(),
                request.getTemplateCode(),
                request.getData(),
                LocalDateTime.now(),
                0
        );
        notificationProducer.send(notificationEvent);



        Notification notification = new Notification();
                notification.setNotificationId(notificationId);
                notification.setChannel(request.getChannel());
                notification.setRecipient(request.getRecipient());
                notification.setTemplateId(request.getTemplateCode());
                notification.setStatus("QUEUED");
                notification.setCreatedAt(LocalDateTime.now());

        try {
            notificationRepository.save(notification);
            log.info("Saved successfully");
        } catch (Exception e) {
            log.error("Save failed", e);
        }

        log.info("Notification queued: {} for channel: {}",
                notificationId, request.getChannel());
        return new NotificationResponse(
                notificationId,
                "QUEUED",
                "Notification queued successfully"
        );
    }

    public Notification getNotificationById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    public void updateStatus(String id, String status) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Notification not found"));

        notification.setStatus(status);

        notificationRepository.save(notification);

        log.info("Notification {} updated to {}",
                id,
                status);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
