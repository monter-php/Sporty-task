package com.tgi.sporty.api.event;

import lombok.Value;

@Value
public class EventStatusUpdateRequest {
    String eventId;
    String status;
}
