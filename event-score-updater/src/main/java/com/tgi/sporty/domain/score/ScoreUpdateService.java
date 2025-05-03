package com.tgi.sporty.domain.score;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreUpdateService {

    private final ScoreRepository scoreRepository;
    private final ScoreUpdateNotifyService scoreUpdateNotifyService;

    public void updateScoreForEvent(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }

        Score score = scoreRepository.getScoreForEvent(eventId);

        if (score == null) {
            throw new ScoreNotFoundException("Score not found for eventId: " + eventId);
        }

        ScoreUpdateMessage message = new ScoreUpdateMessage();
        message.setEventId(score.getEventId());
        message.setCurrentScore(score.getCurrentScore());
        scoreUpdateNotifyService.sendScoreUpdateNotification(message);
    }

}
