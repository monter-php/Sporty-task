package com.tgi.sporty.domain.event;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class EventStatusUpdate {
    String eventId;
    EventStatus eventStatus;
}
