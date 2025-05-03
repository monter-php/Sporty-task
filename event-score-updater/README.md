# Event Score Updater

## Overview
A Java-based microservice for tracking and updating live sports event scores. The service periodically polls an external REST API for event scores, transforms the data, and publishes updates to a message broker (e.g., Kafka). It also exposes a REST endpoint for managing event status (live/not live).

## Features
- Periodically calls an external REST API every 10 seconds for live events
- Transforms and publishes event score updates to a Kafka topic
- Exposes a REST endpoint to receive event status updates
- Basic error handling and logging

## Architecture & Design Choices
- Built with Java and Spring Boot for rapid microservice development
- Uses scheduled tasks for periodic polling
- Kafka for event-driven messaging (can be replaced with other brokers)
- Modular structure for easy testing and extension

more info in [docs/flows.md](docs/flows.md) and [docs/sequences.md](docs/sequences.md)

## Prerequisites
- Java 17+
- Maven 3.6+
- Kafka (local or remote instance)



## Configuration
- Kafka and external API endpoints are configured in `src/main/resources/application.properties`.
- Example API response:
  ```json
  { "eventId": "1234", "currentScore": "0:0" }
  ```


## AI Usage
Some code and documentation were generated or assisted by AI tools (e.g., ChatGPT, GitHub Copilot). All outputs were reviewed and validated.

