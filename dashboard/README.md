# Dashboard

This dashboard allows for the presentation and control of the entire application in order to demonstrate its functionality.
It consists of two modules.
- The first is a user interface using the Streamlit library, which displays information and allows sending status requests for a given event.
- The second is a consumer of Kafka messages with results for each event.

## Main Features

- Enables sending status update requests for an event ("live"/"not live") via a REST interface.
- Displays messages from the Kafka queue in real-time, with results for each event.

