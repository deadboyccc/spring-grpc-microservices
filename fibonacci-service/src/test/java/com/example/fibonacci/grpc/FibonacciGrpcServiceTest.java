package com.example.fibonacci.grpc;

import com.example.fibonacci.messaging.ResultPublisher;
import math.MathRequest;
import math.MathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciGrpcServiceTest {
    private FibonacciGrpcService service;
    private FactorialGrpcClient factorialGrpcClient;
    private ResultPublisher resultPublisher;

    @BeforeEach
    void setUp() {
        factorialGrpcClient = Mockito.mock(FactorialGrpcClient.class);
        resultPublisher = Mockito.mock(ResultPublisher.class);
        service = new FibonacciGrpcService(factorialGrpcClient, resultPublisher);
    }

    @Test
    void testFibonacciSumOf5() {
        Mockito.when(factorialGrpcClient.getFactorial(5)).thenReturn(120L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(5).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(7, response.getResult()); // Sum of first 5 Fibonacci numbers: 0+1+1+2+3 = 7
        assertTrue(response.getAccumulatedResultsList().contains(127L)); // 7+120
    }

    @Test
    void testFibonacciSumOf3() {
        Mockito.when(factorialGrpcClient.getFactorial(3)).thenReturn(6L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(3).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(2, response.getResult()); // Sum of first 3 Fibonacci numbers: 0+1+1 = 2
        assertTrue(response.getAccumulatedResultsList().contains(8L)); // 2+6
    }

    @Test
    void testFibonacciSumOf0() {
        Mockito.when(factorialGrpcClient.getFactorial(0)).thenReturn(1L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(0).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(0, response.getResult()); // Sum of first 0 Fibonacci numbers = 0
        assertTrue(response.getAccumulatedResultsList().contains(1L)); // 0+1
    }

    @Test
    void testFibonacciSumOf1() {
        Mockito.when(factorialGrpcClient.getFactorial(1)).thenReturn(1L);
        TestStreamObserver observer = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(1).build(), observer);
        MathResponse response = observer.getResponse();
        assertEquals(1, response.getResult()); // Sum of first 1 Fibonacci numbers = 1
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
        Mockito.when(factorialGrpcClient.getFactorial(2)).thenReturn(2L);
        Mockito.when(factorialGrpcClient.getFactorial(3)).thenReturn(6L);
        
        TestStreamObserver observer1 = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(2).build(), observer1);
        
        TestStreamObserver observer2 = new TestStreamObserver();
        service.calculate(MathRequest.newBuilder().setNumber(3).build(), observer2);
        
        MathResponse response1 = observer1.getResponse();
        MathResponse response2 = observer2.getResponse();
        
        assertEquals(1, response1.getResult()); // Sum of first 2 Fibonacci numbers: 0+1 = 1
        assertEquals(2, response2.getResult()); // Sum of first 3 Fibonacci numbers: 0+1+1 = 2
        
        // Check accumulated results grow
        assertTrue(response2.getAccumulatedResultsList().size() > response1.getAccumulatedResultsList().size());
    }
} 