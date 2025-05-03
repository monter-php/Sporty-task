# Score Service

This is a simple Python mock service that provides a REST endpoint to manage and update in-memory event scores.

## Technologies Used

- Python
- Flask

## Setup and usage

The score-service is one of the containers in the docker-compose file and does not require manual configuration.
## Endpoint

### `GET /event/score/<eventId>`

Retrieves or updates the score for a given event.

- If the event does not exist, it is created with an initial score of "0:0".
- If the event exists, one of the scores (home or away) is randomly incremented.

**Parameters:**

- `eventId` (string): The unique identifier for the event.

**Response:**

A JSON object with the following structure:

```json
{
	"eventId": "string",
	"currentScore": "string"
}
```

Example:

```json
{
	"eventId": "12345",
	"currentScore": "2:1"
}
```

