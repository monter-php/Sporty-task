import logging
from flask import Flask, jsonify
import random

app = Flask(__name__)

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# In-memory storage for event scores
event_scores = {}

@app.route('/event/score/<string:eventId>', methods=['GET'])
def get_event_score(eventId):
    """
    Gets or updates the score for a given event.
    If the event does not exist, it is created with a score of "0:0".
    If the event exists, the score is randomly incremented.
    """
    logging.info(f"Received request for eventId: {eventId}")
    if eventId not in event_scores:
        logging.info(f"Event {eventId} not found. Creating with score 0:0")
        event_scores[eventId] = "0:0"
    else:
        logging.info(f"Event {eventId} found. Current score: {event_scores[eventId]}")
        current_score = event_scores[eventId].split(':')
        home_score = int(current_score[0])
        away_score = int(current_score[1])

        # Randomly decide if there will be score update
        if random.choice([True, False]):            
            # Randomly increment home or away score
            if random.choice([True, False]):
                home_score += 1
                logging.info(f"Incrementing home score for event {eventId}")
            else:
                away_score += 1
                logging.info(f"Incrementing away score for event {eventId}")
        else:
            logging.info(f"No score update for event {eventId}");
        
        event_scores[eventId] = f"{home_score}:{away_score}"
        logging.info(f"Updated score for event {eventId}: {event_scores[eventId]}")

    response_data = {
        "eventId": eventId,
        "currentScore": event_scores[eventId]
    }

    logging.info(f"Returning response for event {eventId}: {response_data}")

    return jsonify(response_data)

if __name__ == '__main__':
    # Run the Flask development server
    app.run(debug=False, host='0.0.0.0', port=4000)
