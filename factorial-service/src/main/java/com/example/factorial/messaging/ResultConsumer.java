package com.example.factorial.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ResultConsumer {
    @RabbitListener(queues = "results.queue", concurrency = "1")
    public void receive(List<Long> results) {
        log.info("[FactorialService] Received results from queue: {}", results);
    }
} 