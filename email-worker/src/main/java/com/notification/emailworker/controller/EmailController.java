package com.notification.emailworker.controller;

import com.notification.emailworker.entity.EmailTemplate;
import com.notification.emailworker.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/populateEmailTempData")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<EmailTemplate> populateEmailData(@RequestBody EmailTemplate emailTemplate) {
        log.info("Received request for subject: {}", emailTemplate.getSubject());

        emailService.populateEmailData(emailTemplate);
        return ResponseEntity.ok(emailTemplate);
    }
}
