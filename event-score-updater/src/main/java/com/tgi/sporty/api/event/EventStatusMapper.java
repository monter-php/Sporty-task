package com.tgi.sporty.api.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.tgi.sporty.domain.event.EventStatus;
import com.tgi.sporty.domain.event.EventStatusUpdate;

@Mapper(componentModel = "spring")
public interface EventStatusMapper {

    @Mapping(source = "status", target = "eventStatus")
    EventStatusUpdate mapRequest(com.tgi.sporty.api.event.EventStatusUpdateRequest request);

    default EventStatus mapStatus(String status) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.getValue().equalsIgnoreCase(status)) {
                return eventStatus;
            }
        }
        throw new IllegalArgumentException("Invalid EventStatus: " + status);
    }
}
