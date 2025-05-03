package com.tgi.sporty.api.event;

import com.tgi.sporty.domain.event.EventStatus;

import lombok.Value;

@Value
public class EventStatusUpdateResponse {
    String eventId;
    EventStatus status;
}
