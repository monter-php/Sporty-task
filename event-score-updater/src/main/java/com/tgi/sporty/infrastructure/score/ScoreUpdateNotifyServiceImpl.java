package com.tgi.sporty.infrastructure.score;

import com.tgi.sporty.domain.score.ScoreUpdateMessage;
import com.tgi.sporty.domain.score.ScoreUpdateNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScoreUpdateNotifyServiceImpl implements ScoreUpdateNotifyService {

    private final KafkaTemplate<String, ScoreUpdateMessage> kafkaTemplate;
    private final String scoreUpdateTopic;

    public ScoreUpdateNotifyServiceImpl(KafkaTemplate<String, ScoreUpdateMessage> kafkaTemplate,
                                        @Value("${kafka.score.update.topic}") String scoreUpdateTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.scoreUpdateTopic = scoreUpdateTopic;
    }

    @Override
    public void sendScoreUpdateNotification(ScoreUpdateMessage scoreUpdateMessage) {
        log.info("Sending score update notification: {}", scoreUpdateMessage);
        kafkaTemplate.send(scoreUpdateTopic, scoreUpdateMessage);
    }
}
