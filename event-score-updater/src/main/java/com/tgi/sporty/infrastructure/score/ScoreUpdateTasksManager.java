package com.tgi.sporty.infrastructure.score;

import com.tgi.sporty.domain.score.ScoreUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreUpdateTasksManager {
    private final ThreadPoolTaskScheduler taskScheduler;
    private final ScoreUpdateService scoreUpdateService;

    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    public void startTask(String taskId) {
        log.info("Starting task: {}", taskId);
        ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(
                () -> scoreUpdateService.updateScoreForEvent(taskId),
                Duration.ofSeconds(10)
        );
        tasks.put(taskId, future);
    }

    public void stopTask(String taskId) {
        log.info("Stopping task: {}", taskId);
        ScheduledFuture<?> future = tasks.get(taskId);
        if (future != null) {
            future.cancel(true);
            tasks.remove(taskId);
        }
    }

}
