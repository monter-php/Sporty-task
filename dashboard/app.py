import streamlit as st
import requests
import json

import time
import pandas as pd


st.set_page_config(layout="wide")

st.title("SportyTask Dashboard")

# Create two columns
col1, col2 = st.columns(2)

with col1:
    st.header("Control Event Status")
    st.info("Use fields below to add and update status of given event. You can out arbitrary Event ID.")

    event_id = st.text_input("Event ID")
    status = st.selectbox("Status", ["live", "not_live"])

    EVENTS_FILE = 'events.json'

    def load_event_statuses():
        try:
            with open(EVENTS_FILE, 'r') as f:
                return json.load(f)
        except (FileNotFoundError, json.JSONDecodeError):
            return {}

    def save_event_statuses(statuses):
        with open(EVENTS_FILE, 'w') as f:
            json.dump(statuses, f)

    # Load event statuses when the app starts
    event_statuses = load_event_statuses()

    if st.button("Update Event Status"):
        if event_id:
            try:
                # Assuming event-score-updater is accessible at http://event-score-updater:8080
                # This URL will need to be correct based on the docker-compose setup
                response = requests.post("http://event-score-updater:8080/events/status", json={"eventId": event_id, "status": status})
                if response.status_code == 200:
                    # Update and save event statuses
                    event_statuses[event_id] = status
                    save_event_statuses(event_statuses)
                    st.success(f"Successfully updated status for Event ID {event_id} to {event_statuses[event_id]}")
                else:
                    st.error(f"Failed to update status. Status code: {response.status_code}")
                    st.error(f"Response: {response.text}")
            except requests.exceptions.RequestException as e:
                st.error(f"An error occurred while connecting to the event score updater: {e}")
        else:
            st.warning("Please enter an Event ID")

    st.subheader("Current Event Statuses")
    st.info("This is list of events that was added with their coresponding status")

    if event_statuses:
        # Convert the dictionary to a list of dictionaries for pandas DataFrame
        event_status_list = [{"Event ID": eid, "Status": status} for eid, status in event_statuses.items()]
        df_statuses = pd.DataFrame(event_status_list)
        st.table(df_statuses)
    else:
        st.write("No event statuses updated yet.")


with col2:
    st.header("Live Scores (Kafka Consumer)")
    st.info("Here are the live results displayed, received from the Kafka queue.")

    SCORES_FILE = 'scores.json'

    st.write("Live scores will appear here...")

    def load_scores():
        try:
            with open(SCORES_FILE, 'r') as f:
                return json.load(f)
        except (FileNotFoundError, json.JSONDecodeError):
            return {}

    st.subheader("Remove Event Score")

    remove_event_id = st.text_input("Event ID to Remove")
    if st.button("Remove Score"):
        if remove_event_id:
            scores = load_scores()
            if remove_event_id in scores:
                del scores[remove_event_id]
                with open(SCORES_FILE, 'w') as f:
                    json.dump(scores, f)
                st.success(f"Successfully removed score for Event ID {remove_event_id}")
            else:
                st.warning(f"Event ID {remove_event_id} not found in scores.")
        else:
            st.warning("Please enter an Event ID to remove.")


    st.subheader("Events Scores")

    scores_placeholder = st.empty()



    # Periodically update the scores display
    while True:
        scores = load_scores()
        if scores and isinstance(scores, dict):
            # Assuming scores is a dictionary like {"event1": "score1", "event2": "score2", ...}
            # Convert to a list of dictionaries for pandas DataFrame
            scores_list = [{"Event ID": event_id, "Current Score": score} for event_id, score in scores.items()]
            if scores_list:
                df = pd.DataFrame(scores_list)
                scores_placeholder.table(df)
            else:
                scores_placeholder.write("Waiting for scores...")
        else:
            scores_placeholder.write("Waiting for scores...")

        time.sleep(1) # Refresh every 1 second
