package com.example.factorial.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultPublisher {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "results.exchange";
    private static final String ROUTING_KEY = "results.key";

    public void publish(List<Long> results) {
        try {
            log.debug("Publishing results to RabbitMQ: {}", results);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, results);
            log.info("Successfully published {} results to RabbitMQ", results.size());
        } catch (Exception e) {
            log.error("Error publishing results to RabbitMQ: {}", results, e);
            throw new RuntimeException("Failed to publish results to RabbitMQ", e);
        }
    }
} 