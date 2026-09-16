package com.notification.smsworkers.consumer;

import com.notification.smsworkers.dto.NotificationEvent;
import com.notification.smsworkers.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class NotificationConsumer {

    @Autowired
    private final SmsService smsService;

    public NotificationConsumer(SmsService smsService) {
        this.smsService = smsService;
    }

    @KafkaListener(
            topics = "${notification.kafka.topic}",
            groupId = "sms-worker-group"
    )
    public void consume(NotificationEvent event) {

        log.info("Received notification {}",
                event.getNotificationId());
        if (!"sms".equalsIgnoreCase(event.getChannel())) {
            System.out.println("Skipping non-SMS notification: " + event.getChannel());
            return;
        }

        smsService.sendSms(event);
    }
}