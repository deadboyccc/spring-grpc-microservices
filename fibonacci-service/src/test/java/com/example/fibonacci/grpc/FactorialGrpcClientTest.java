package com.example.fibonacci.grpc;

import math.MathRequest;
import math.MathResponse;
import math.FactorialServiceGrpc;
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
class FactorialGrpcClientTest {

    @Mock
    private FactorialServiceGrpc.FactorialServiceBlockingStub mockStub;

    private FactorialGrpcClient client;

    @BeforeEach
    void setUp() {
        client = new FactorialGrpcClient("localhost", 9090);
        ReflectionTestUtils.setField(client, "stub", mockStub);
    }

    @Test
    void testGetFactorial() {
        // Given
        int input = 5;
        MathResponse expectedResponse = MathResponse.newBuilder()
                .setResult(120L)
                .build();
        when(mockStub.calculate(any(MathRequest.class))).thenReturn(expectedResponse);

        // When
        long result = client.getFactorial(input);

        // Then
        assertEquals(120L, result);
    }

    @Test
    void testGetFactorialWithZero() {
        // Given
        int input = 0;
        MathResponse expectedResponse = MathResponse.newBuilder()
                .setResult(1L)
                .build();
        when(mockStub.calculate(any(MathRequest.class))).thenReturn(expectedResponse);

        // When
        long result = client.getFactorial(input);

        // Then
        assertEquals(1L, result);
    }
} 