package com.example.fibonacci.grpc;

import com.example.fibonacci.messaging.ResultPublisher;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import math.FibonacciServiceGrpc;
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
public class FibonacciGrpcService extends FibonacciServiceGrpc.FibonacciServiceImplBase {
  private final FactorialGrpcClient factorialGrpcClient;
  private final ResultPublisher resultPublisher;
  private final List<Long> accumulatedResults = new CopyOnWriteArrayList<>();

  // Maximum safe Fibonacci sum value to prevent overflow
  private static final int MAX_SAFE_FIBONACCI = 92; // Sum of first 92 Fibonacci numbers fits in long

  @Override
  public void calculate(MathRequest request, StreamObserver<MathResponse> responseObserver) {
    try {
      int n = request.getNumber();
      log.info("Calculating Fibonacci sum for number: {}", n);

      if (n < 0) {
        log.warn("Negative number received: {}", n);
        responseObserver.onError(new IllegalArgumentException("Fibonacci sum is not defined for negative numbers"));
        return;
      }

      if (n > MAX_SAFE_FIBONACCI) {
        log.warn("Number too large for Fibonacci sum calculation: {}", n);
        responseObserver.onError(new IllegalArgumentException(
            "Number too large for Fibonacci sum calculation. Maximum allowed: " + MAX_SAFE_FIBONACCI));
        return;
      }

      long result = fibonacciSum(n);
      log.debug("Fibonacci sum of {} = {}", n, result);

      long factResult = factorialGrpcClient.getFactorial(n);
      log.debug("Factorial of {} = {}", n, factResult);

      long sum = result + factResult;
      accumulatedResults.add(sum);

      MathResponse response = MathResponse.newBuilder()
          .setResult(result)
          .addAllAccumulatedResults(accumulatedResults)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

      log.info("Successfully calculated Fibonacci sum for number: {}, result: {}", n, result);
    } catch (Exception e) {
      log.error("Error calculating Fibonacci sum for request: {}", request, e);
      responseObserver.onError(e);
    }
  }

  private long fibonacciSum(int n) {
    if (n == 0) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }

    long a = 0, b = 1, sum = 0;
    for (int i = 1; i <= n; i++) {
      // Check for overflow
      if (sum > Long.MAX_VALUE - a) {
        throw new ArithmeticException("Fibonacci sum calculation would overflow for input: " + n);
      }
      sum += a;
      long next = a + b;
      // Check for overflow in next calculation
      if (a > Long.MAX_VALUE - b) {
        throw new ArithmeticException("Fibonacci calculation would overflow for input: " + n);
      }
      a = b;
      b = next;
    }
    return sum;
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