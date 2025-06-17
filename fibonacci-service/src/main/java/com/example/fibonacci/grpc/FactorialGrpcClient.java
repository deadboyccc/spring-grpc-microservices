package com.example.fibonacci.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import math.FactorialServiceGrpc;
import math.MathRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class FactorialGrpcClient {
    private final FactorialServiceGrpc.FactorialServiceBlockingStub stub;
    private final ManagedChannel channel;

    public FactorialGrpcClient(@Value("${factorial.grpc.host:factorial-service}") String host,
                               @Value("${factorial.grpc.port:9090}") int port) {
        log.info("Initializing Factorial gRPC client to {}:{}", host, port);
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = FactorialServiceGrpc.newBlockingStub(channel);
        log.info("Factorial gRPC client initialized successfully");
    }

    public long getFactorial(int n) {
        try {
            log.debug("Requesting Factorial calculation for number: {}", n);
            MathRequest request = MathRequest.newBuilder().setNumber(n).build();
            long result = stub.calculate(request).getResult();
            log.debug("Factorial calculation result for {}: {}", n, result);
            return result;
        } catch (Exception e) {
            log.error("Error calling Factorial service for number: {}", n, e);
            throw new RuntimeException("Failed to get Factorial result", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            log.info("Shutting down Factorial gRPC client");
            channel.shutdown();
        }
    }
} 