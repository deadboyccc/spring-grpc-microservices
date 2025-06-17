package com.example.fibonacci.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange resultsExchange() {
        return new TopicExchange("results.exchange");
    }
} 