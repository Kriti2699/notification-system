package com.notification.notificationapi.dto;

import lombok.Data;

import java.util.Map;
//This class represents the input from the client.
@Data
public class NotificationRequest {

    private String channel;        // email, sms, push
    private String recipient;      // email address or phone number
    private String templateId;     // which template to use
    private Map<String, String> data;  // template variables
}
