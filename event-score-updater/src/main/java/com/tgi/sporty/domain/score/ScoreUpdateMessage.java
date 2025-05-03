package com.tgi.sporty.domain.score;

import lombok.Data;

@Data
public class ScoreUpdateMessage {
    String eventId;
    String currentScore;
}
