package com.tgi.sporty.api.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgi.sporty.domain.event.EventStatus;
import com.tgi.sporty.domain.event.EventStatusService;
import com.tgi.sporty.domain.event.EventStatusUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = com.tgi.sporty.application.SportyApplication.class)
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventStatusService eventStatusService;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EventStatusMapper eventStatusMapper;

    @Test
    public void testCreateEvent() throws Exception {
        EventStatusUpdateRequest eventData = new EventStatusUpdateRequest("123", "live");
        EventStatus updatedStatus = EventStatus.LIVE;

        when(eventStatusService.updateEventStatus(any(EventStatusUpdate.class))).thenReturn(updatedStatus);

        mockMvc.perform(post("/events/status")
                        .content(asJsonString(eventData))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("123"));

        verify(eventStatusService).updateEventStatus(any(EventStatusUpdate.class));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateEvent_invalidStatus() throws Exception {
        String invalidStatusJson = "{\"eventId\": \"123\", \"status\": \"invalid\"}";

        mockMvc.perform(post("/events/status")
                        .content(invalidStatusJson)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(eventStatusService);
    }
}
