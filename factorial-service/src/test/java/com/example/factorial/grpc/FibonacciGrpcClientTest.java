package com.example.factorial.grpc;

import math.MathRequest;
import math.MathResponse;
import math.FibonacciServiceGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FibonacciGrpcClientTest {

    @Mock
    private FibonacciServiceGrpc.FibonacciServiceBlockingStub mockStub;

    private FibonacciGrpcClient client;

    @BeforeEach
    void setUp() {
        client = new FibonacciGrpcClient("localhost", 9090);
        ReflectionTestUtils.setField(client, "stub", mockStub);
    }

    @Test
    void testGetFibonacci() {
        // Given
        int input = 5;
        MathResponse expectedResponse = MathResponse.newBuilder()
                .setResult(5L)
                .build();
        when(mockStub.calculate(any(MathRequest.class))).thenReturn(expectedResponse);

        // When
        long result = client.getFibonacci(input);

        // Then
        assertEquals(5L, result);
    }

    @Test
    void testGetFibonacciWithZero() {
        // Given
        int input = 0;
        MathResponse expectedResponse = MathResponse.newBuilder()
                .setResult(0L)
                .build();
        when(mockStub.calculate(any(MathRequest.class))).thenReturn(expectedResponse);

        // When
        long result = client.getFibonacci(input);

        // Then
        assertEquals(0L, result);
    }
} 