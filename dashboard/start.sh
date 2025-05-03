#!/bin/bash

# Start the Kafka consumer in the background
python kafka_consumer.py &

# Start the Streamlit app
streamlit run app.py --server.port 8501 --server.address 0.0.0.0
