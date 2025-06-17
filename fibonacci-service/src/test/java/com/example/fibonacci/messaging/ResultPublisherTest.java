package com.example.fibonacci.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResultPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private ResultPublisher resultPublisher;

    @BeforeEach
    void setUp() {
        resultPublisher = new ResultPublisher(rabbitTemplate);
    }

    @Test
    void testPublish() {
        // Given
        List<Long> results = Arrays.asList(7L, 120L, 127L);

        // When
        resultPublisher.publish(results);

        // Then
        verify(rabbitTemplate).convertAndSend("results.exchange", "results.key", results);
    }

    @Test
    void testPublishEmptyList() {
        // Given
        List<Long> results = Arrays.asList();

        // When
        resultPublisher.publish(results);

        // Then
        verify(rabbitTemplate).convertAndSend("results.exchange", "results.key", results);
    }
} 