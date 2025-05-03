package com.tgi.sporty.domain.score;

public class ScoreNotFoundException extends RuntimeException {
    public ScoreNotFoundException(String message) {
        super(message);
    }
}
