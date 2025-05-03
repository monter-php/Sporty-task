package com.tgi.sporty.infrastructure.score;

import com.tgi.sporty.domain.score.Score;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScoreRestRepositoryTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RetryTemplate retryTemplate;

    private final ScoreServiceParameters scoreServiceParameters = new ScoreServiceParameters("http", "localhost", "8080");

    private ScoreRestRepository scoreRestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scoreRestRepository = new ScoreRestRepository(restTemplate, retryTemplate, scoreServiceParameters);
    }

    @Test
    void getScoreForEvent_success() {
        // Arrange
        String eventId = "event123";
        Score mockScore = new Score();
        String url = scoreServiceParameters.getScoreServiceUrl(eventId);

        when(restTemplate.getForObject(url, Score.class)).thenReturn(mockScore);
        when(retryTemplate.execute(any(RetryCallback.class))).thenAnswer(invocation -> {
            RetryCallback<Object, Exception> callback = invocation.getArgument(0);
            return callback.doWithRetry(mock(RetryContext.class));
        });

        // Act
        Score result = scoreRestRepository.getScoreForEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(mockScore, result);
        verify(restTemplate, times(1)).getForObject(url, Score.class);
        verify(retryTemplate, times(1)).execute(any(RetryCallback.class));
    }

    @Test
    void getScoreForEvent_nullEventId_throwsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                scoreRestRepository.getScoreForEvent(null));
        assertEquals("Event ID cannot be null or empty", ex.getMessage());
    }

    @Test
    void getScoreForEvent_emptyEventId_throwsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                scoreRestRepository.getScoreForEvent("   "));
        assertEquals("Event ID cannot be null or empty", ex.getMessage());
    }

    @Test
    void getScoreForEvent_restTemplateThrowsException_returnsNull() {
        // Arrange
        String eventId = "event123";
        String url = scoreServiceParameters.getScoreServiceUrl(eventId);

        when(restTemplate.getForObject(url, Score.class)).thenThrow(new RuntimeException("REST error"));
        when(retryTemplate.execute(any(RetryCallback.class))).thenAnswer(invocation -> {
            RetryCallback<Object, Exception> callback = invocation.getArgument(0);
            try {
                return callback.doWithRetry(mock(RetryContext.class));
            } catch (Exception e) {
                throw e; // Let the repository's catch block handle it
            }
        });

        // Act
        Score result = scoreRestRepository.getScoreForEvent(eventId);

        // Assert
        assertNull(result);
        verify(restTemplate, times(1)).getForObject(url, Score.class);
        verify(retryTemplate, times(1)).execute(any(RetryCallback.class));
    }

    @Test
    void getScoreForEvent_retryOnFailure() {
        // Arrange
        String eventId = "event123";
        Score mockScore = new Score();
        String url = scoreServiceParameters.getScoreServiceUrl(eventId);

        // First call throws exception, second call returns mockScore
        when(restTemplate.getForObject(url, Score.class))
                .thenThrow(new RuntimeException("Temporary error"))
                .thenReturn(mockScore);

        // Mock retry behavior
        when(retryTemplate.execute(any(RetryCallback.class))).thenAnswer(invocation -> {
            RetryCallback<Object, Exception> callback = invocation.getArgument(0);
            RetryContext mockContext = mock(RetryContext.class);
            when(mockContext.getRetryCount()).thenReturn(1); // Simulate second attempt

            try {
                return callback.doWithRetry(mockContext);
            } catch (Exception e) {
                // In a real RetryTemplate, it would retry after exception
                // Here we'll simulate that by calling the callback again
                when(mockContext.getRetryCount()).thenReturn(1);
                return callback.doWithRetry(mockContext);
            }
        });

        // Act
        Score result = scoreRestRepository.getScoreForEvent(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(mockScore, result);
        verify(retryTemplate, times(1)).execute(any(RetryCallback.class));
        // RestTemplate should be called twice (first fails, second succeeds)
        verify(restTemplate, times(2)).getForObject(url, Score.class);
    }
}
