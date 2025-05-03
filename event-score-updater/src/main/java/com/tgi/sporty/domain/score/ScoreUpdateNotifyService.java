package com.tgi.sporty.domain.score;

public interface ScoreUpdateNotifyService {
    void sendScoreUpdateNotification(ScoreUpdateMessage scoreUpdateMessage);
}
