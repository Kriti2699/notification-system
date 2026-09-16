package com.notification.smsworkers.controller;

import com.notification.smsworkers.entity.SmsTemplate;
import com.notification.smsworkers.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/populateSMSData")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @PostMapping
    public ResponseEntity<SmsTemplate> populateSmsData(@RequestBody SmsTemplate smsTemplate) {
        log.info("Received request for subject: {}", smsTemplate.getMessage());

        smsService.populatesmsData(smsTemplate);
        return ResponseEntity.ok(smsTemplate);
    }
}
