package com.tgi.sporty.infrastructure.score;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "score.service")
public class ScoreServiceParameters {
    private static final String SCORE_SERVICE_URL_PATTERN = "%s://%s:%s/event/score/%s";

    String protocol;
    String host;
    String port;

    public String getScoreServiceUrl(String eventId) {
        return String.format(SCORE_SERVICE_URL_PATTERN, protocol, host, port, eventId);
    }
}
