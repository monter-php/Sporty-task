package com.tgi.sporty.domain.event;

public interface EventStatusRepository {
    EventStatus getEventStatus(String eventId);

    void setEventStatus(String eventId, EventStatus status);
}
