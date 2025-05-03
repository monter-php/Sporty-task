# SportyTask

This project consists of three main components: an event score updater, a dashboard and a score service.

## Event Score Updater

### Overview
A Java-based microservice for tracking and updating live sports event scores. The service periodically polls an external REST API for active events scores, transforms the data, and publishes updates to a message broker (e.g., Kafka). It also exposes a REST endpoint for managing event status (live/not live).

### Features
- Periodically calls an external REST API every 10 seconds for live events
- Transforms and publishes event score updates to a Kafka topic
- Exposes a REST endpoint to receive event status updates
- Basic error handling and logging


## Dashboard

This dashboard allows for the presentation and control of the entire application in order to demonstrate its functionality.
It consists of two modules: a user interface using the Streamlit library, which displays information and allows sending status requests for a given event, and a consumer of Kafka messages with results for each event.

### Main Features

- Enables sending status update requests for an event ("live"/"not live") via a REST interface.
- Displays messages from the Kafka queue in real-time, with results for each event.

## Score Service

This is a simple Python mock service that provides a REST endpoint to manage and update in-memory event scores.

## Setup and Run

This project is designed to be run using Docker Compose.

1. Ensure you have Docker and Docker Compose installed.
2. Navigate to the root directory of the project in your terminal.
3. Run the following command to build and start the services:

```sh
docker-compose up --build
```

This will build the necessary Docker images and start the `dashboard`, `score-service`, and `event-score-updater` containers, along with Kafka and Zookeeper.

4. Once the services are running, you can access the dashboard in your web browser, typically at `http://localhost:8501`.

## Running tests

To run unit and integration tests in the Event Score Updater service, follow these steps:

1. You must have Maven installed and configured.
2. Navigate to the `event-score-updater` directory.
3. Execute the following commands:
	1. `mvn clean install`
	2. `mvn test`
