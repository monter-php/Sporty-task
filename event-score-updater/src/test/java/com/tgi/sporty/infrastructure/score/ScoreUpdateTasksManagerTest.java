package com.tgi.sporty.infrastructure.score;

import com.tgi.sporty.domain.score.ScoreUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ScoreUpdateTasksManagerTest {
    private ThreadPoolTaskScheduler taskScheduler;
    private ScoreUpdateService scoreUpdateService;
    private ScoreUpdateTasksManager manager;

    @BeforeEach
    void setUp() {
        taskScheduler = mock(ThreadPoolTaskScheduler.class);
        scoreUpdateService = mock(ScoreUpdateService.class);
        manager = new ScoreUpdateTasksManager(taskScheduler, scoreUpdateService);
    }

    @Test
    void startTask_schedulesTaskAndStoresFuture() {
        String taskId = "event-123";
        @SuppressWarnings("unchecked")
        ScheduledFuture<Object> mockFuture = mock(ScheduledFuture.class);
        when((ScheduledFuture<Object>) taskScheduler.scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10))))
                .thenReturn(mockFuture);

        manager.startTask(taskId);

        verify(taskScheduler).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10)));
        // Start task again should replace the old one
        manager.startTask(taskId);
        verify(taskScheduler, times(2)).scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10)));
    }

    @Test
    void stopTask_cancelsAndRemovesFuture() {
        String taskId = "event-123";
        @SuppressWarnings("unchecked")
        ScheduledFuture<Object> mockFuture = mock(ScheduledFuture.class);
        when((ScheduledFuture<Object>) taskScheduler.scheduleAtFixedRate(any(Runnable.class), eq(Duration.ofSeconds(10))))
                .thenReturn(mockFuture);
        manager.startTask(taskId);

        manager.stopTask(taskId);
        verify(mockFuture).cancel(true);
        // Stopping again should not throw
        manager.stopTask(taskId);
    }

    @Test
    void startTask_runsScoreUpdateService() {
        String taskId = "event-123";
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        @SuppressWarnings("unchecked")
        ScheduledFuture<Object> mockFuture = mock(ScheduledFuture.class);
        when((ScheduledFuture<Object>) taskScheduler.scheduleAtFixedRate(runnableCaptor.capture(), eq(Duration.ofSeconds(10))))
                .thenReturn(mockFuture);

        manager.startTask(taskId);
        Runnable scheduledRunnable = runnableCaptor.getValue();
        assertNotNull(scheduledRunnable);
        scheduledRunnable.run();
        verify(scoreUpdateService).updateScoreForEvent(taskId);
    }
}
