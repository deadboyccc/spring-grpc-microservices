import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import math.FactorialServiceGrpc;
import math.FibonacciServiceGrpc;
import math.MathRequest;

import java.util.concurrent.TimeUnit;

/**
 * Simple test client for manual testing of the gRPC services
 * This is a learning tool to demonstrate how to call the services
 */
public class TestClient {
  private final ManagedChannel factorialChannel;
  private final ManagedChannel fibonacciChannel;
  private final FactorialServiceGrpc.FactorialServiceBlockingStub factorialStub;
  private final FibonacciServiceGrpc.FibonacciServiceBlockingStub fibonacciStub;

  public TestClient() {
    // Connect to factorial service
    this.factorialChannel = ManagedChannelBuilder.forAddress("localhost", 9090)
        .usePlaintext()
        .build();
    this.factorialStub = FactorialServiceGrpc.newBlockingStub(factorialChannel);

    // Connect to fibonacci service
    this.fibonacciChannel = ManagedChannelBuilder.forAddress("localhost", 9091)
        .usePlaintext()
        .build();
    this.fibonacciStub = FibonacciServiceGrpc.newBlockingStub(fibonacciChannel);
  }

  public void testFactorialService(int number) {
    System.out.println("=== Testing Factorial Service ===");
    try {
      MathRequest request = MathRequest.newBuilder().setNumber(number).build();
      var response = factorialStub.calculate(request);

      System.out.println("Input: " + number);
      System.out.println("Factorial Result: " + response.getResult());
      System.out.println("Accumulated Results: " + response.getAccumulatedResultsList());
      System.out.println();
    } catch (Exception e) {
      System.err.println("Error calling factorial service: " + e.getMessage());
    }
  }

  public void testFibonacciService(int number) {
    System.out.println("=== Testing Fibonacci Service ===");
    try {
      MathRequest request = MathRequest.newBuilder().setNumber(number).build();
      var response = fibonacciStub.calculate(request);

      System.out.println("Input: " + number);
      System.out.println("Fibonacci Sum Result: " + response.getResult());
      System.out.println("Accumulated Results: " + response.getAccumulatedResultsList());
      System.out.println();
    } catch (Exception e) {
      System.err.println("Error calling fibonacci service: " + e.getMessage());
    }
  }

  public void shutdown() {
    try {
      factorialChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
      fibonacciChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public static void main(String[] args) {
    TestClient client = new TestClient();

    try {
      // Test with different numbers
      int[] testNumbers = { 0, 1, 3, 5, 7 };

      for (int num : testNumbers) {
        client.testFactorialService(num);
        client.testFibonacciService(num);
        System.out.println("----------------------------------------");
      }

    } finally {
      client.shutdown();
    }
  }
}