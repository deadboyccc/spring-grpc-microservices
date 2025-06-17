package com.example.factorial.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitQueueConfig {
    @Bean
    public Queue resultsQueue() {
        return new Queue("results.queue", false);
    }

    @Bean
    public Binding binding(Queue resultsQueue, TopicExchange resultsExchange) {
        return BindingBuilder.bind(resultsQueue).to(resultsExchange).with("results.key");
    }
} 