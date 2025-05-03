package com.tgi.sporty.domain.event;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.tgi.sporty.infrastructure.score.ScoreUpdateTasksManager;

@Service
@RequiredArgsConstructor
public class EventStatusService {

    private final EventStatusRepository eventStatusRepository;
    private final ScoreUpdateTasksManager scoreUpdateTasksManager;

    public EventStatus updateEventStatus(EventStatusUpdate eventStatusUpdate) {
        // Validate input
        if (eventStatusUpdate == null) {
            throw new IllegalArgumentException("EventStatusUpdate cannot be null");
        }
        
        String eventId = eventStatusUpdate.getEventId();
        EventStatus newStatus = eventStatusUpdate.getEventStatus();

        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("New Event Status cannot be null");
        }

        // get event status from EventStatusRepository
        EventStatus currentStatus = eventStatusRepository.getEventStatus(eventId);

        // check if status is different from eventStatusUpdate variable
        if (currentStatus == null || !currentStatus.equals(newStatus)) {
            // if differ update event in EventStatusRepository
            eventStatusRepository.setEventStatus(eventId, newStatus);

            // if event is live, then start scheduled thread to call score update
            if (newStatus == EventStatus.LIVE) {
                scoreUpdateTasksManager.startTask(eventId);
            }
            // if event is not-live then check if score update thread is running and stop it if needed
            else if (newStatus == EventStatus.NOT_LIVE) {
                scoreUpdateTasksManager.stopTask(eventId);
            }
        }

        // Return updated EventStatus
        return newStatus;
    }
}
