package com.notification.notificationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
//This is the object that is published to Kafka.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String notificationId;
    private String channel;
    private String recipient;
    private String templateId;
    private Map<String, String> data;
    private LocalDateTime createdAt;
    private int retryCount;
}
