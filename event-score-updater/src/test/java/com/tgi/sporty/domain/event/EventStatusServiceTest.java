package com.tgi.sporty.domain.event;

import com.tgi.sporty.infrastructure.score.ScoreUpdateTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EventStatusServiceTest {

    @Mock
    private EventStatusRepository eventStatusRepository;

    @Mock
    private ScoreUpdateTasksManager scoreUpdateTasksManager;

    @InjectMocks
    private EventStatusService eventStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateEventStatus_ValidUpdate_StatusChanged_NotLiveToLive() {
        // Arrange
        String eventId = "event1";
        EventStatus currentStatus = EventStatus.NOT_LIVE;
        EventStatus newStatus = EventStatus.LIVE;
        EventStatusUpdate update = new EventStatusUpdate(eventId, newStatus);

        when(eventStatusRepository.getEventStatus(eventId)).thenReturn(currentStatus);

        // Act
        EventStatus updatedStatus = eventStatusService.updateEventStatus(update);

        // Assert
        assertEquals(newStatus, updatedStatus);
        verify(eventStatusRepository).setEventStatus(eventId, newStatus);
        verify(scoreUpdateTasksManager).startTask(eventId);
        verify(scoreUpdateTasksManager, never()).stopTask(eventId);
    }

    @Test
    void testUpdateEventStatus_ValidUpdate_StatusChanged_LiveToNotLive() {
        // Arrange
        String eventId = "event2";
        EventStatus currentStatus = EventStatus.LIVE;
        EventStatus newStatus = EventStatus.NOT_LIVE;
        EventStatusUpdate update = new EventStatusUpdate(eventId, newStatus);

        when(eventStatusRepository.getEventStatus(eventId)).thenReturn(currentStatus);

        // Act
        EventStatus updatedStatus = eventStatusService.updateEventStatus(update);

        // Assert
        assertEquals(newStatus, updatedStatus);
        verify(eventStatusRepository).setEventStatus(eventId, newStatus);
        verify(scoreUpdateTasksManager, never()).startTask(eventId);
        verify(scoreUpdateTasksManager).stopTask(eventId);
    }

    @Test
    void testUpdateEventStatus_ValidUpdate_StatusNotChanged() {
        // Arrange
        String eventId = "event1";
        EventStatus currentStatus = EventStatus.LIVE;
        EventStatus newStatus = EventStatus.LIVE;
        EventStatusUpdate update = new EventStatusUpdate(eventId, newStatus);

        when(eventStatusRepository.getEventStatus(eventId)).thenReturn(currentStatus);

        // Act
        EventStatus updatedStatus = eventStatusService.updateEventStatus(update);

        // Assert
        assertEquals(newStatus, updatedStatus);
        verify(eventStatusRepository, never()).setEventStatus(anyString(), any());
        verify(scoreUpdateTasksManager, never()).startTask(anyString());
        verify(scoreUpdateTasksManager, never()).stopTask(anyString());
    }

    @Test
    void testUpdateEventStatus_NullEventStatusUpdate_ThrowsIllegalArgumentException() {
        // Arrange
        EventStatusUpdate update = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventStatusService.updateEventStatus(update));
        assertEquals("EventStatusUpdate cannot be null", exception.getMessage());
        verify(eventStatusRepository, never()).getEventStatus(anyString());
        verify(eventStatusRepository, never()).setEventStatus(anyString(), any());
        verify(scoreUpdateTasksManager, never()).startTask(anyString());
        verify(scoreUpdateTasksManager, never()).stopTask(anyString());
    }

    @Test
    void testUpdateEventStatus_NullEventId_ThrowsIllegalArgumentException() {
        // Arrange
        EventStatusUpdate update = new EventStatusUpdate(null, EventStatus.LIVE);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventStatusService.updateEventStatus(update));
        assertEquals("Event ID cannot be null or empty", exception.getMessage());
        verify(eventStatusRepository, never()).getEventStatus(anyString());
        verify(eventStatusRepository, never()).setEventStatus(anyString(), any());
        verify(scoreUpdateTasksManager, never()).startTask(anyString());
        verify(scoreUpdateTasksManager, never()).stopTask(anyString());
    }

    @Test
    void testUpdateEventStatus_EmptyEventId_ThrowsIllegalArgumentException() {
        // Arrange
        EventStatusUpdate update = new EventStatusUpdate("", EventStatus.LIVE);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventStatusService.updateEventStatus(update));
        assertEquals("Event ID cannot be null or empty", exception.getMessage());
        verify(eventStatusRepository, never()).getEventStatus(anyString());
        verify(eventStatusRepository, never()).setEventStatus(anyString(), any());
        verify(scoreUpdateTasksManager, never()).startTask(anyString());
        verify(scoreUpdateTasksManager, never()).stopTask(anyString());
    }

    @Test
    void testUpdateEventStatus_NullNewStatus_ThrowsIllegalArgumentException() {
        // Arrange
        EventStatusUpdate update = new EventStatusUpdate("event1", null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                eventStatusService.updateEventStatus(update));
        assertEquals("New Event Status cannot be null", exception.getMessage());
        verify(eventStatusRepository, never()).getEventStatus(anyString());
        verify(eventStatusRepository, never()).setEventStatus(anyString(), any());
        verify(scoreUpdateTasksManager, never()).startTask(anyString());
        verify(scoreUpdateTasksManager, never()).stopTask(anyString());
    }
}