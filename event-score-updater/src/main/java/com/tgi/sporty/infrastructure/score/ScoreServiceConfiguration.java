package com.tgi.sporty.infrastructure.score;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ScoreServiceParameters.class)
public class ScoreServiceConfiguration {
}
