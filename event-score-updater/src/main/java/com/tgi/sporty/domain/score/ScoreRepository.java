package com.tgi.sporty.domain.score;

public interface ScoreRepository {
    Score getScoreForEvent(String eventId);
}
