package com.tgi.sporty.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventStatusUpdate {
    String eventId;
    EventStatus eventStatus;
}
