package com.notification.emailworker.consumer;

import com.notification.emailworker.dto.NotificationEvent;
import com.notification.emailworker.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${notification.kafka.topic}",
            groupId = "email-worker-group"
    )
    public void consume(NotificationEvent event) {

        log.info("Received notification {}",
                event.getNotificationId());

        emailService.sendEmail(event);
    }
}