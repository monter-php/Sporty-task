package com.tgi.sporty.domain.score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScoreUpdateServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private ScoreUpdateNotifyService scoreUpdateNotifyService;

    @InjectMocks
    private ScoreUpdateService scoreUpdateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateScoreForEvent_ValidEventId_ScoreUpdatedAndNotified() {
        // Arrange
        String eventId = "event1";
        Score mockScore = new Score(); // Assuming Score has a no-arg constructor or setters
        mockScore.setEventId(eventId);
        mockScore.setCurrentScore("1:0"); // Set the score as a String

        when(scoreRepository.getScoreForEvent(eventId)).thenReturn(mockScore);

        // Act
        scoreUpdateService.updateScoreForEvent(eventId);

        // Assert
        verify(scoreRepository).getScoreForEvent(eventId);
        verify(scoreUpdateNotifyService).sendScoreUpdateNotification(any(ScoreUpdateMessage.class));
    }

    @Test
    void testUpdateScoreForEvent_NullEventId_ThrowsIllegalArgumentException() {
        // Arrange
        String eventId = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scoreUpdateService.updateScoreForEvent(eventId));
        assertEquals("Event ID cannot be null or empty", exception.getMessage());
        verify(scoreRepository, never()).getScoreForEvent(anyString());
        verify(scoreUpdateNotifyService, never()).sendScoreUpdateNotification(any(ScoreUpdateMessage.class));
    }

    @Test
    void testUpdateScoreForEvent_EmptyEventId_ThrowsIllegalArgumentException() {
        // Arrange
        String eventId = "";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                scoreUpdateService.updateScoreForEvent(eventId));
        assertEquals("Event ID cannot be null or empty", exception.getMessage());
        verify(scoreRepository, never()).getScoreForEvent(anyString());
        verify(scoreUpdateNotifyService, never()).sendScoreUpdateNotification(any(ScoreUpdateMessage.class));
    }

    @Test
    void testUpdateScoreForEvent_ScoreNotFound_LogsWarningAndDoesNotNotify() {
        // Arrange
        String eventId = "nonexistentEvent";
        when(scoreRepository.getScoreForEvent(eventId)).thenReturn(null);

        // Act
        scoreUpdateService.updateScoreForEvent(eventId);

        // Assert
        verify(scoreRepository).getScoreForEvent(eventId);
        verify(scoreUpdateNotifyService, never()).sendScoreUpdateNotification(any(ScoreUpdateMessage.class));
        // Note: Verifying log messages with Mockito requires additional setup (e.g., using Logback test appenders)
        // This test primarily verifies that notification is NOT sent when score is null.
    }
}