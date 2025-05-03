package com.tgi.sporty.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tgi.sporty.application", "com.tgi.sporty.api", "com.tgi.sporty.domain", "com.tgi.sporty.infrastructure"})
public class SportyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportyApplication.class, args);
    }
}
