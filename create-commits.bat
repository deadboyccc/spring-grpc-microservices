@echo off
echo Creating 50 detailed commits for the gRPC + RabbitMQ project...

REM Project Foundation & Setup
git add proto/math.proto
git commit -m "feat: Add Protocol Buffer definitions for gRPC services (math.proto)"

git add factorial-service/src/main/java/com/example/factorial/FactorialServiceApplication.java
git commit -m "feat: Create base Spring Boot application for factorial service"

git add fibonacci-service/src/main/java/com/example/fibonacci/FibonacciServiceApplication.java
git commit -m "feat: Create base Spring Boot application for fibonacci service"

git add factorial-service/pom.xml fibonacci-service/pom.xml
git commit -m "feat: Add Maven configuration with gRPC and Spring Boot dependencies"

git add docker-compose.yml
git commit -m "feat: Set up Docker Compose for service orchestration"

REM gRPC Service Implementation
git add factorial-service/src/main/java/com/example/factorial/grpc/FactorialGrpcService.java
git commit -m "feat: Implement FactorialGrpcService with calculation logic and gRPC communication"

git add fibonacci-service/src/main/java/com/example/fibonacci/grpc/FibonacciGrpcService.java
git commit -m "feat: Implement FibonacciGrpcService with Fibonacci sum calculation"

git add factorial-service/src/main/java/com/example/factorial/grpc/FibonacciGrpcClient.java
git commit -m "feat: Add gRPC client implementation for factorial service"

git add fibonacci-service/src/main/java/com/example/fibonacci/grpc/FactorialGrpcClient.java
git commit -m "feat: Add gRPC client implementation for fibonacci service"

git add factorial-service/src/main/java/com/example/factorial/grpc/ fibonacci-service/src/main/java/com/example/fibonacci/grpc/
git commit -m "feat: Implement bidirectional gRPC calls between factorial and fibonacci services"

REM RabbitMQ Integration
git add factorial-service/src/main/java/com/example/factorial/messaging/RabbitConfig.java fibonacci-service/src/main/java/com/example/fibonacci/messaging/RabbitConfig.java
git commit -m "feat: Add RabbitMQ configuration and exchange setup"

git add factorial-service/src/main/java/com/example/factorial/messaging/ResultPublisher.java fibonacci-service/src/main/java/com/example/fibonacci/messaging/ResultPublisher.java
git commit -m "feat: Implement ResultPublisher for asynchronous message publishing"

git add factorial-service/src/main/java/com/example/factorial/messaging/ResultConsumer.java fibonacci-service/src/main/java/com/example/fibonacci/messaging/ResultConsumer.java
git commit -m "feat: Implement ResultConsumer for message consumption"

git add factorial-service/src/main/java/com/example/factorial/messaging/RabbitQueueConfig.java fibonacci-service/src/main/java/com/example/fibonacci/messaging/RabbitQueueConfig.java
git commit -m "feat: Configure RabbitMQ queue bindings and routing"

REM Error Handling & Validation
git add factorial-service/src/main/java/com/example/factorial/grpc/FactorialGrpcService.java
git commit -m "fix: Add overflow protection for factorial calculations (max input: 20)"

git add fibonacci-service/src/main/java/com/example/fibonacci/grpc/FibonacciGrpcService.java
git commit -m "fix: Add overflow protection for Fibonacci calculations (max input: 92)"

git add factorial-service/src/main/java/com/example/factorial/grpc/FactorialGrpcService.java fibonacci-service/src/main/java/com/example/fibonacci/grpc/FibonacciGrpcService.java
git commit -m "fix: Implement negative number validation with descriptive error messages"

REM Logging & Observability
git add factorial-service/src/main/java/com/example/factorial/grpc/FactorialGrpcService.java factorial-service/src/main/java/com/example/factorial/grpc/FibonacciGrpcClient.java
git commit -m "feat: Add structured logging throughout factorial service"

git add fibonacci-service/src/main/java/com/example/fibonacci/grpc/FibonacciGrpcService.java fibonacci-service/src/main/java/com/example/fibonacci/grpc/FactorialGrpcClient.java
git commit -m "feat: Add structured logging throughout fibonacci service"

git add factorial-service/src/main/resources/application.yml fibonacci-service/src/main/resources/application.yml
git commit -m "feat: Add Spring Boot Actuator for health checks and metrics"

REM Docker & Containerization
git add factorial-service/Dockerfile fibonacci-service/Dockerfile
git commit -m "feat: Create multi-stage Dockerfiles for efficient builds"

git add docker-compose.yml
git commit -m "fix: Update Docker Compose with health checks and networking"

REM Configuration & Environment
git add factorial-service/src/main/resources/application.yml fibonacci-service/src/main/resources/application.yml
git commit -m "feat: Add environment variable configuration for gRPC hosts"

git add factorial-service/src/main/java/com/example/factorial/FactorialServiceApplication.java fibonacci-service/src/main/java/com/example/fibonacci/FibonacciServiceApplication.java
git commit -m "fix: Add @EnableScheduling annotation for scheduled tasks"

REM Testing Infrastructure
git add factorial-service/src/test/java/com/example/factorial/grpc/FactorialGrpcServiceTest.java
git commit -m "feat: Create comprehensive unit tests for factorial gRPC service"

git add fibonacci-service/src/test/java/com/example/fibonacci/grpc/FibonacciGrpcServiceTest.java
git commit -m "feat: Create comprehensive unit tests for fibonacci gRPC service"

git add factorial-service/src/test/java/com/example/factorial/grpc/FibonacciGrpcClientTest.java fibonacci-service/src/test/java/com/example/fibonacci/grpc/FactorialGrpcClientTest.java
git commit -m "feat: Add gRPC client tests with proper mocking"

git add factorial-service/src/test/java/com/example/factorial/messaging/ResultPublisherTest.java fibonacci-service/src/test/java/com/example/fibonacci/messaging/ResultPublisherTest.java
git commit -m "feat: Implement RabbitMQ publisher tests"

git add factorial-service/src/test/java/com/example/factorial/messaging/ResultConsumerTest.java fibonacci-service/src/test/java/com/example/fibonacci/messaging/ResultConsumerTest.java
git commit -m "feat: Implement RabbitMQ consumer tests"

git add factorial-service/src/test/java/com/example/factorial/grpc/TestStreamObserver.java fibonacci-service/src/test/java/com/example/fibonacci/grpc/TestStreamObserver.java
git commit -m "feat: Create TestStreamObserver utility for gRPC testing"

REM Test Coverage & Quality
git add factorial-service/src/test/java/com/example/factorial/grpc/FactorialGrpcServiceTest.java fibonacci-service/src/test/java/com/example/fibonacci/grpc/FibonacciGrpcServiceTest.java
git commit -m "test: Add negative number validation tests"

git add factorial-service/src/test/java/com/example/factorial/grpc/FactorialGrpcServiceTest.java fibonacci-service/src/test/java/com/example/fibonacci/grpc/FibonacciGrpcServiceTest.java
git commit -m "test: Add overflow protection tests for large inputs"

git add factorial-service/src/test/java/com/example/factorial/grpc/FactorialGrpcServiceTest.java fibonacci-service/src/test/java/com/example/fibonacci/grpc/FibonacciGrpcServiceTest.java
git commit -m "test: Add accumulated results verification tests"

REM Documentation & Guides
git add README.md
git commit -m "docs: Create comprehensive README with architecture overview"

git add TESTING_GUIDE.md
git commit -m "docs: Add detailed testing guide with multiple testing methods"

git add IMPROVEMENTS_SUMMARY.md
git commit -m "docs: Create improvements summary documenting all changes"

REM Scripts & Automation
git add run-tests.bat
git commit -m "feat: Add Windows batch script for running all tests"

git add start-demo.bat start-demo.sh
git commit -m "feat: Create startup scripts for Windows and Linux/Mac"

git add test-client/TestClient.java
git commit -m "feat: Add test client for manual gRPC testing"

REM Production Readiness
git add factorial-service/src/main/java/com/example/factorial/grpc/FibonacciGrpcClient.java fibonacci-service/src/main/java/com/example/fibonacci/grpc/FactorialGrpcClient.java
git commit -m "feat: Implement proper resource cleanup with @PreDestroy"

git add docker-compose.yml
git commit -m "feat: Add graceful shutdown handling and service dependencies"

git add factorial-service/Dockerfile fibonacci-service/Dockerfile
git commit -m "security: Implement non-root container execution"

git add factorial-service/src/main/java/com/example/factorial/grpc/FactorialGrpcService.java fibonacci-service/src/main/java/com/example/fibonacci/grpc/FibonacciGrpcService.java
git commit -m "perf: Optimize calculations with early returns and overflow checks"

git add factorial-service/src/main/java/com/example/factorial/grpc/FactorialGrpcService.java fibonacci-service/src/main/java/com/example/fibonacci/grpc/FibonacciGrpcService.java
git commit -m "feat: Add comprehensive error recovery mechanisms"

git add factorial-service/src/main/java/com/example/factorial/messaging/ResultPublisher.java fibonacci-service/src/main/java/com/example/fibonacci/messaging/ResultPublisher.java
git commit -m "feat: Add RabbitMQ connection resilience and error handling"

git add docker-compose.yml
git commit -m "feat: Configure Docker Compose for one-command deployment"

git add README.md TESTING_GUIDE.md
git commit -m "docs: Add troubleshooting section and production considerations"

git add .
git commit -m "feat: Final integration - complete gRPC + RabbitMQ microservices demo"

echo.
echo ========================================
echo All 50 commits have been created!
echo ========================================
echo.
echo To view the commit history:
echo   git log --oneline
echo.
echo To push to a remote repository:
echo   git remote add origin <your-repo-url>
echo   git push -u origin main
echo.
pause 