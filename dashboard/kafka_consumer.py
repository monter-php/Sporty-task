import json
import time
from kafka import KafkaConsumer

# Configuration
KAFKA_BROKER = 'kafka:9092' # Assuming Kafka service name in docker-compose is 'kafka'
KAFKA_TOPIC = 'score-updates' # Assuming the topic name is 'scores'
SCORES_FILE = 'scores.json' # File to write scores to

def consume_scores():
    consumer = None
    while consumer is None:
        try:
            consumer = KafkaConsumer(
                KAFKA_TOPIC,
                bootstrap_servers=[KAFKA_BROKER],
                auto_offset_reset='latest', # Start consuming from the latest message
                enable_auto_commit=True,
                group_id='sporty',
                value_deserializer=lambda x: json.loads(x.decode('utf-8'))
            )
            print("Kafka consumer connected successfully.")
        except Exception as e:
            print(f"Could not connect to Kafka broker {KAFKA_BROKER}. Retrying in 5 seconds... Error: {e}")
            time.sleep(5)

    print(f"Starting to consume messages from topic {KAFKA_TOPIC}")
    try:
        for message in consumer:
            print(f"Received message: {message.value}")
            # In a real application, you might process the score data
            # Load existing scores, update with the new message, and save
            scores = {}
            try:
                with open(SCORES_FILE, 'r') as f:
                    scores = json.load(f)
            except (FileNotFoundError, json.JSONDecodeError):
                pass # Start with an empty dictionary if file doesn't exist or is empty

            # Assuming message.value is a dictionary like {"eventId": "...", "currentScore": ...}
            if isinstance(message.value, dict) and "eventId" in message.value and "currentScore" in message.value:
                event_id = message.value["eventId"]
                current_score = message.value["currentScore"]
                scores[event_id] = current_score # Store the latest score for this event

                with open(SCORES_FILE, 'w') as f:
                    json.dump(scores, f)
                print(f"Updated score for event {event_id} and wrote to {SCORES_FILE}")
            else:
                print(f"Received message with unexpected format: {message.value}")

    except Exception as e:
        print(f"Error during Kafka consumption: {e}")
    finally:
        if consumer:
            consumer.close()
            print("Kafka consumer closed.")

if __name__ == "__main__":
    # Ensure the scores file exists initially with an empty dictionary
    try:
        with open(SCORES_FILE, 'x') as f:
            json.dump({}, f)
        print(f"Created initial empty {SCORES_FILE}")
    except FileExistsError:
        print(f"{SCORES_FILE} already exists.")
        # Ensure it contains a valid JSON object (preferably a dictionary)
        try:
            with open(SCORES_FILE, 'r') as f:
                content = json.load(f)
                if not isinstance(content, dict):
                    print(f"{SCORES_FILE} exists but does not contain a dictionary. Overwriting with empty dictionary.")
                    with open(SCORES_FILE, 'w') as f_write:
                        json.dump({}, f_write)
        except json.JSONDecodeError:
             print(f"{SCORES_FILE} exists but contains invalid JSON. Overwriting with empty dictionary.")
             with open(SCORES_FILE, 'w') as f_write:
                json.dump({}, f_write)
        except Exception as e:
            print(f"Error checking/initializing {SCORES_FILE}: {e}")


    consume_scores()
