package com.notification.emailworker.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_template")
public class EmailTemplate {

    @Id
    private String tempId;
    private String tempCode;
    private String subject;
    private String html_file;
    private String status;
    private LocalDateTime createdAt;

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getTempCode() {
        return tempCode;
    }

    public void setTempCode(String tempCode) {
        this.tempCode = tempCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtml_file() {
        return html_file;
    }

    public void setHtml_file(String html_file) {
        this.html_file = html_file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
