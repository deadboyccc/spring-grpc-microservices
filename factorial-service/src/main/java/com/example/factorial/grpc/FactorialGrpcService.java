package com.example.factorial.grpc;

import com.example.factorial.messaging.ResultPublisher;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import math.FactorialServiceGrpc;
import math.Math.MathRequest;
import math.Math.MathResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class FactorialGrpcService extends FactorialServiceGrpc.FactorialServiceImplBase {
  private final FibonacciGrpcClient fibonacciGrpcClient;
  private final ResultPublisher resultPublisher;
  private final List<Long> accumulatedResults = new CopyOnWriteArrayList<>();

  // Maximum safe factorial value to prevent overflow
  private static final int MAX_SAFE_FACTORIAL = 20; // 20! is the largest factorial that fits in long

  @Override
  public void calculate(MathRequest request, StreamObserver<MathResponse> responseObserver) {
    try {
      int n = request.getNumber();
      log.info("Calculating factorial for number: {}", n);

      if (n < 0) {
        log.warn("Negative number received: {}", n);
        responseObserver.onError(new IllegalArgumentException("Factorial is not defined for negative numbers"));
        return;
      }

      if (n > MAX_SAFE_FACTORIAL) {
        log.warn("Number too large for factorial calculation: {}", n);
        responseObserver.onError(new IllegalArgumentException(
            "Number too large for factorial calculation. Maximum allowed: " + MAX_SAFE_FACTORIAL));
        return;
      }

      long result = factorial(n);
      log.debug("Factorial of {} = {}", n, result);

      long fibResult = fibonacciGrpcClient.getFibonacci(n);
      log.debug("Fibonacci sum of {} = {}", n, fibResult);

      long sum = result + fibResult;
      accumulatedResults.add(sum);

      MathResponse response = MathResponse.newBuilder()
          .setResult(result)
          .addAllAccumulatedResults(accumulatedResults)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

      log.info("Successfully calculated factorial for number: {}, result: {}", n, result);
    } catch (Exception e) {
      log.error("Error calculating factorial for request: {}", request, e);
      responseObserver.onError(e);
    }
  }

  private long factorial(int n) {
    if (n == 0 || n == 1) {
      return 1;
    }
    long res = 1;
    for (int i = 2; i <= n; i++) {
      // Check for overflow
      if (res > Long.MAX_VALUE / i) {
        throw new ArithmeticException("Factorial calculation would overflow for input: " + n);
      }
      res *= i;
    }
    return res;
  }

  @Scheduled(fixedRate = 30000)
  public void publishResults() {
    try {
      if (!accumulatedResults.isEmpty()) {
        resultPublisher.publish(accumulatedResults);
        log.info("Published accumulated results: {}", accumulatedResults);
      } else {
        log.debug("No accumulated results to publish");
      }
    } catch (Exception e) {
      log.error("Error publishing accumulated results", e);
    }
  }
}