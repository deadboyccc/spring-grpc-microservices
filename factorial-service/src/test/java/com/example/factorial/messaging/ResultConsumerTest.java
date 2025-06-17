package com.example.factorial.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ResultConsumerTest {

    @InjectMocks
    private ResultConsumer resultConsumer;

    @BeforeEach
    void setUp() {
        // No setup needed for this simple consumer
    }

    @Test
    void testReceive() {
        // Given
        List<Long> results = Arrays.asList(120L, 5L, 125L);

        // When & Then - should not throw any exception
        resultConsumer.receive(results);
    }

    @Test
    void testReceiveEmptyList() {
        // Given
        List<Long> results = Arrays.asList();

        // When & Then - should not throw any exception
        resultConsumer.receive(results);
    }
} 