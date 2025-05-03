package com.tgi.sporty.api.event;

import com.tgi.sporty.domain.event.EventStatus;
import com.tgi.sporty.domain.event.EventStatusService;
import com.tgi.sporty.domain.event.EventStatusUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventStatusService eventStatusService;

    private final EventStatusMapper eventStatusMapper;

    @PostMapping("/events/status")
    public ResponseEntity<EventStatusUpdateResponse> updateStatusEvent(@RequestBody EventStatusUpdateRequest eventData) {
            EventStatusUpdate eventStatusUpdate = eventStatusMapper.mapRequest(eventData);
            EventStatus eventStatus = eventStatusService.updateEventStatus(eventStatusUpdate);
            return ResponseEntity.ok(new EventStatusUpdateResponse(eventData.getEventId(), eventStatus));
    }
}
