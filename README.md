# Notification System - Microservices

A Spring Boot microservices-based notification system that processes notifications asynchronously using Apache Kafka and sends emails using configurable HTML templates.

## Architecture

```
                    +----------------+
                    |     Client     |
                    +--------+-------+
                             |
                             v
                  +----------------------+
                  |   Notification API   |
                  |      (Port 8081)     |
                  +----------+-----------+
                             |
              Save Notification (QUEUED)
                             |
                             v
                    +----------------+
                    |     Kafka      |
                    | notifications  |
                    |    .pending    |
                    +--------+-------+
                             |
                             v
                  +----------------------+
                  |    Email Worker      |
                  |      (Port 8083)     |
                  +----------+-----------+
                             |
               Fetch Template & Send Email
                             |
                             v
                    Update Notification Status
```

---

# Features

- Spring Boot Microservices
- Apache Kafka for asynchronous messaging
- PostgreSQL database
- HTML Email Templates
- JavaMailSender integration
- REST APIs
- Notification Status Tracking
- Template-based email generation
- Maven build
- Docker support

---

# Tech Stack

| Technology | Version |
|------------|----------|
| Java | 17 |
| Spring Boot | 2.7.x |
| Spring Data JPA | Yes |
| PostgreSQL | 14+ |
| Apache Kafka | 7.x |
| Maven | Latest |
| Docker | Yes |

---

# Project Structure

```
notification-system/
│
├── notification-api/
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   ├── kafka
│   └── config
│
├── email-worker/
│   ├── consumer
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── config
│
└── docker-compose.yml
```

---

# Notification Flow

1. Client sends a notification request.
2. Notification API stores the notification with status **QUEUED**.
3. Notification API publishes a `NotificationEvent` to Kafka.
4. Email Worker consumes the event.
5. Email template is loaded from the database.
6. Placeholders are replaced with dynamic values.
7. Email is sent.
8. Email Worker updates the notification status.

---

# Services

## Notification API

**Port**

```
8081
```

Responsibilities:

- Accept notification requests
- Store notifications
- Publish Kafka events
- Update notification status

---

## Email Worker

**Port**

```
8083
```

Responsibilities:

- Consume Kafka events
- Read email templates
- Send emails
- Update notification status

---

# Kafka Configuration

Topic

```
notifications.pending
```

Consumer Group

```
email-worker-group
```

---

# Database

Database

```
notifydb
```

Tables

- notification
- email_template
- emailstats

---

# API Endpoints

## Create Notification

```
POST /notify
```

Example Request

```json
{
  "recipient": "user@example.com",
  "channel": "EMAIL",
  "templateId": "WELCOME",
  "data": {
    "name": "John"
  }
}
```

---

## Update Status

```
PUT /notify/{id}/status
```

---

# Running the Project

## Clone Repository

```bash
git clone https://github.com/your-username/notification-system.git
```

---

## Start PostgreSQL & Kafka

```bash
docker-compose up -d
```

---

## Run Notification API

```bash
cd notification-api
mvn spring-boot:run
```

---

## Run Email Worker

```bash
cd email-worker
mvn spring-boot:run
```

---

# Future Enhancements

- SMS Notifications
- Push Notifications (Firebase)
- Retry Mechanism
- Dead Letter Queue (DLQ)
- Scheduler for failed notifications
- Template Management APIs
- Authentication & Authorization
- Monitoring with Prometheus and Grafana
- Distributed Tracing

---

# Author

**Kriti Sharma**

Backend Developer | Java | Spring Boot | Kafka | PostgreSQL | Microservices

