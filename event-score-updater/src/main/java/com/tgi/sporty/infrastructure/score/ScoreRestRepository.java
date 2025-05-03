package com.tgi.sporty.infrastructure.score;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.retry.support.RetryTemplate;

import com.tgi.sporty.domain.score.Score;
import com.tgi.sporty.domain.score.ScoreRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScoreRestRepository implements ScoreRepository {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final ScoreServiceParameters scoreServiceParameters;


    @Override
    public Score getScoreForEvent(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
        String url = scoreServiceParameters.getScoreServiceUrl(eventId);
        
        try {
            log.info("Fetching score from URL: {}", url);
            return retryTemplate.execute(context -> {
                int retryCount = context.getRetryCount();
                if (retryCount > 0) {
                    log.info("Retry attempt {} for eventId {}", retryCount, eventId);
                }
                try {
                    return restTemplate.getForObject(url, Score.class);
                } catch (Exception e) {
                    log.warn("Attempt {} failed for eventId {}: {}", retryCount + 1, eventId, e.getMessage());
                    throw e;
                }
            });
        } catch (Exception e) {
            log.error("All retry attempts failed to fetch score for eventId {}: {}", eventId, e.getMessage());
            return null; 
        }
    }
}
