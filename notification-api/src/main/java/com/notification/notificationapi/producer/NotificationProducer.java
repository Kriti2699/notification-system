package com.notification.notificationapi.producer;

import com.notification.notificationapi.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {
    @Value("${notification.kafka.topic}")
    private String topic;
//    private static final String TOPIC = "notifications.pending";
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void send(NotificationEvent event) {
        kafkaTemplate.send(topic, event.getNotificationId(), event)
                .addCallback(
                        result -> log.info("Event sent to Kafka: {}", event.getNotificationId()),
                        ex -> log.error("Failed to send event to Kafka: {}", ex.getMessage())
                );
    }
}
