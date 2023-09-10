# g-pubsub-publisher
# Google PubSub Publisher

This repository contains the Google PubSub Publisher service, a service built with Kotlin, Spring Boot, and PostgreSQL. This service essentially handles location information and triggers an event when changes occur in a location which is then published to a Google Cloud Topic.

## Features

1. Listen for changes in location.
2. Emit an event to a Google Cloud Topic when a change is detected.

## Requirements

- JDK 11
- Kotlin
- Spring Boot
- PostgreSQL
- Google Cloud Pub/Sub

## Getting Started

### Setting Up

1. `git clone https://github.com/<yourusername>/google-pubsub-publisher.git`
2. `cd google-pubsub-publisher`
3. Open the project in your favorite IDE.

### Configuring

You need to configure your database credentials and Google Cloud Pub/Sub in the application properties file.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/yourDB
    username: yourUsername
    password: yourPassword
```
To allow your application to authenticate with the Google Cloud, you'll need to provide your own Google Cloud service account key.

1. Download your `json` key file from the Google Cloud Console under IAM & Admin > Service Accounts.

2. Place the key file in the `src/main/resources` directory of your project.

3. Update `application.properties` file with the following properties:

```yaml
spring.cloud.gcp.project-id=yourGoogleCloudProjectId
spring.cloud.gcp.credentials.location=classpath:yourGoogleCloudCredentials.json
```

Example: 
```yaml
spring.cloud.gcp.project-id=codewithsohrab
spring.cloud.gcp.credentials.location=classpath:codewithsohrab-c8c320d9c539.json
```

### Building

To compile source code and build run:

```shell
./mvnw clean package
```
