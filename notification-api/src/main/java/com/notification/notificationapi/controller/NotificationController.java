package com.notification.notificationapi.controller;

import com.notification.notificationapi.dto.NotificationRequest;
import com.notification.notificationapi.dto.NotificationResponse;
import com.notification.notificationapi.dto.StatusUpdateRequest;
import com.notification.notificationapi.entity.Notification;
import com.notification.notificationapi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Send a notification
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @RequestBody NotificationRequest request) {

        log.info("Received notification request for channel: {}", request.getChannel());

        NotificationResponse response = notificationService.send(request);

        if ("FAILED".equalsIgnoreCase(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Get all notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {

        log.info("Fetching all notifications");
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    /**
     * Get notification by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(
            @PathVariable String id) {

        log.info("Fetching notification with id: {}", id);
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String id,
            @RequestBody StatusUpdateRequest request) {

        notificationService.updateStatus(id, request.getStatus());

        return ResponseEntity.ok("Status Updated Successfully");
    }

    /**
     * Health Check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification API is running");
    }
}