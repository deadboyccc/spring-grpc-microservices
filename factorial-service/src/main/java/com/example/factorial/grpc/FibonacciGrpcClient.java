package com.example.factorial.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import math.FibonacciServiceGrpc;
import math.MathRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class FibonacciGrpcClient {
    private final FibonacciServiceGrpc.FibonacciServiceBlockingStub stub;
    private final ManagedChannel channel;

    public FibonacciGrpcClient(@Value("${fibonacci.grpc.host:fibonacci-service}") String host,
                               @Value("${fibonacci.grpc.port:9090}") int port) {
        log.info("Initializing Fibonacci gRPC client to {}:{}", host, port);
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = FibonacciServiceGrpc.newBlockingStub(channel);
        log.info("Fibonacci gRPC client initialized successfully");
    }

    public long getFibonacci(int n) {
        try {
            log.debug("Requesting Fibonacci calculation for number: {}", n);
            MathRequest request = MathRequest.newBuilder().setNumber(n).build();
            long result = stub.calculate(request).getResult();
            log.debug("Fibonacci calculation result for {}: {}", n, result);
            return result;
        } catch (Exception e) {
            log.error("Error calling Fibonacci service for number: {}", n, e);
            throw new RuntimeException("Failed to get Fibonacci result", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            log.info("Shutting down Fibonacci gRPC client");
            channel.shutdown();
        }
    }
} 