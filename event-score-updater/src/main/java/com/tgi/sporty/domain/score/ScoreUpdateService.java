package com.tgi.sporty.domain.score;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreUpdateService {

    private final ScoreRepository scoreRepository;
    private final ScoreUpdateNotifyService scoreUpdateNotifyService;

    public void updateScoreForEvent(String eventId) {
        // Validate input
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }

        // get current score from scoreRepository
        Score score = scoreRepository.getScoreForEvent(eventId);

        // send updated score to Kafka
        if (score != null) {
            ScoreUpdateMessage message = new ScoreUpdateMessage();
            message.setEventId(score.getEventId());
            message.setCurrentScore(score.getCurrentScore());
            scoreUpdateNotifyService.sendScoreUpdateNotification(message);
        } else {
            // Enhance null check: log a warning or throw an exception
            log.warn("Score not found for eventId: {}", eventId);
            // Or throw new ScoreNotFoundException("Score not found for eventId: " + eventId);
        }
    }

}
