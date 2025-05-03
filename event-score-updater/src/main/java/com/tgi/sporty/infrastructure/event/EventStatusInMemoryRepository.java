package com.tgi.sporty.infrastructure.event;

import com.tgi.sporty.domain.event.EventStatus;
import com.tgi.sporty.domain.event.EventStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EventStatusInMemoryRepository implements EventStatusRepository {

    private final Map<String, EventStatus> eventStatuses = new ConcurrentHashMap<>();

    @Override
    public void setEventStatus(String eventId, EventStatus eventStatus) {
        eventStatuses.put(eventId, eventStatus);
    }

    @Override
    public EventStatus getEventStatus(String eventId) {
        return eventStatuses.get(eventId);
    }
}
