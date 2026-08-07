package com.notification.emailworker.entity;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "emailstats")

public class EmailLog {

    @Id
    private String id;
    private int status;
    private String failurereason;
    private String toEmail;
    private LocalDateTime createdAt;
    private String templaesend   ;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFailurereason() {
        return failurereason;
    }

    public void setFailurereason(String failurereason) {
        this.failurereason = failurereason;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getTemplaesend() {
        return templaesend;
    }

    public void setTemplaesend(String templaesend) {
        this.templaesend = templaesend;
    }
}
