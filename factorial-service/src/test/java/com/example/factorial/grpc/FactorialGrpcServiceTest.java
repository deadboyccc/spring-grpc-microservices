package com.example.factorial.grpc;

import com.example.factorial.messaging.ResultPublisher;
import math.MathRequest;
import math.MathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FactorialGrpcServiceTest {
    private FactorialGrpcService service;
    private FibonacciGrpcClient fibonacciGrpcClient;
    private ResultPublisher resultPublisher;

    @BeforeEach
    void setUp() {
        fibonacciGrpcClient = Mockito.mock(FibonacciGrpcClient.class);
        resultPublisher = Mockito.mock(ResultPublisher.class);
        service = new FactorialGrpcService(fibonacciGrpcClient, resultPublisher);
    }

    @Test
    void testFactorialOf5() {
        Mockito.when(fibonacciGrpcClient.getFibonacci(5)).thenReturn(5L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(5).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(120, response.getResult()); // 5! = 120
        assertTrue(response.getAccumulatedResultsList().contains(125L)); // 120+5
    }

    @Test
    void testFactorialOf0() {
        Mockito.when(fibonacciGrpcClient.getFibonacci(0)).thenReturn(0L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(0).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(1, response.getResult()); // 0! = 1
        assertTrue(response.getAccumulatedResultsList().contains(1L)); // 1+0
    }

    @Test
    void testFactorialOf1() {
        Mockito.when(fibonacciGrpcClient.getFibonacci(1)).thenReturn(1L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(1).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(1, response.getResult()); // 1! = 1
        assertTrue(response.getAccumulatedResultsList().contains(2L)); // 1+1
    }

    @Test
    void testNegativeNumber() {
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(-1).build(), observer);
        assertTrue(observer.isError());
    }

    @Test
    void testAccumulatedResults() {
        Mockito.when(fibonacciGrpcClient.getFibonacci(2)).thenReturn(2L);
        Mockito.when(fibonacciGrpcClient.getFibonacci(3)).thenReturn(3L);
        
        TestStreamObserver observer1 = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(2).build(), observer1);
        
        TestStreamObserver observer2 = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(3).build(), observer2);
        
        MathResponse response1 = observer1.getResponse();
        MathResponse response2 = observer2.getResponse();
        
        assertEquals(2, response1.getResult()); // 2! = 2
        assertEquals(6, response2.getResult()); // 3! = 6
        
        // Check accumulated results grow
        assertTrue(response2.getAccumulatedResultsList().size() > response1.getAccumulatedResultsList().size());
    }
} 