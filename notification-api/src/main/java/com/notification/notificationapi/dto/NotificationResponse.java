package com.notification.notificationapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
//This is sent back to the client.
@Data
@AllArgsConstructor
public class NotificationResponse {

    private String notificationId;
    private String status;
    private String message;
}
