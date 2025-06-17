# gRPC + RabbitMQ Microservices Demo

> 🎵 **Vibe Coded Disclaimer:** This entire project was vibe coded with Cursor! ✨  
> *When the coding vibes are right, magic happens. This demo is the result of pure vibes and some serious microservices energy.* 🚀

A **complete, production-ready learning demo** that demonstrates modern microservices architecture using **gRPC** for inter-service communication and **RabbitMQ** for asynchronous messaging.

## 🎯 What This Demo Teaches

This demo showcases:
- **gRPC** - High-performance, type-safe service-to-service communication
- **RabbitMQ** - Asynchronous messaging and event-driven architecture
- **Microservices** - Service decomposition, containerization, and orchestration
- **Spring Boot** - Modern Java application development
- **Docker** - Containerization and service orchestration
- **Testing** - Comprehensive unit and integration testing strategies

## 🏗️ Architecture Overview

```
┌─────────────────┐    gRPC     ┌─────────────────┐
│  Factorial      │◄───────────►│   Fibonacci     │
│   Service       │             │    Service      │
│  (Port 9090)    │             │   (Port 9091)   │
└─────────────────┘             └─────────────────┘
         │                               │
         │                               │
         ▼                               ▼
┌─────────────────────────────────────────────────┐
│              RabbitMQ Broker                    │
│  ┌─────────────┐    ┌─────────────┐            │
│  │   Exchange  │    │    Queue    │            │
│  │  (Topic)    │───►│  (Results)  │            │
│  └─────────────┘    └─────────────┘            │
└─────────────────────────────────────────────────┘
```

## 📋 How It Works

### Factorial Service
- **Calculates factorial** of a given number (e.g., 5! = 120)
- **Calls Fibonacci service** via gRPC to get Fibonacci sum
- **Combines results** (factorial + Fibonacci sum)
- **Publishes results** to RabbitMQ every 30 seconds
- **Input validation**: Accepts numbers 0-20 (prevents overflow)

### Fibonacci Service  
- **Calculates Fibonacci sum** of first N numbers (e.g., sum of first 5 Fibonacci numbers: 0+1+1+2+3 = 7)
- **Calls Factorial service** via gRPC to get factorial
- **Combines results** (Fibonacci sum + factorial)
- **Publishes results** to RabbitMQ every 30 seconds
- **Input validation**: Accepts numbers 0-92 (prevents overflow)

## 🚀 Quick Start

### Prerequisites
- **Docker** and **Docker Compose**
- **Java 17+** (for local development)
- **Maven 3.6+** (for local development)

### One-Command Setup

```bash
# Clone and navigate to the project
cd spring-grpc-microservices

# Start everything with Docker Compose
docker-compose up --build
```

That's it! The demo will:
1. ✅ Build both microservices from source
2. ✅ Start RabbitMQ with management UI
3. ✅ Start both services with proper networking
4. ✅ Set up health checks and service discovery
5. ✅ Begin publishing messages to RabbitMQ

### Verify Everything is Running

Check these URLs in your browser:

- **RabbitMQ Management UI:** http://localhost:15672
  - Username: `guest` | Password: `guest`
  - You should see `results.queue` and `results.exchange`

- **Factorial Service Health:** http://localhost:8080/actuator/health
  - Should return: `{"status":"UP"}`

- **Fibonacci Service Health:** http://localhost:8081/actuator/health
  - Should return: `{"status":"UP"}`

## 🧪 Testing the Services

### Method 1: Using grpcurl (Recommended)

#### Install grpcurl

**Windows:**
```bash
choco install grpcurl
```

**Mac:**
```bash
brew install grpcurl
```

**Linux:**
```bash
# Download from https://github.com/fullstorydev/grpcurl/releases
```

#### Test the Services

```bash
# Test Factorial Service
grpcurl -plaintext -d '{"number": 5}' localhost:9090 math.FactorialService/Calculate

# Expected response:
# {
#   "result": 120,
#   "accumulatedResults": [125]
# }

# Test Fibonacci Service
grpcurl -plaintext -d '{"number": 5}' localhost:9091 math.FibonacciService/Calculate

# Expected response:
# {
#   "result": 7,
#   "accumulatedResults": [127]
# }
```

### Method 2: Using the Test Client

```bash
# Compile and run the test client
cd test-client
javac -cp ".:../factorial-service/target/classes:../fibonacci-service/target/classes" TestClient.java
java -cp ".:../factorial-service/target/classes:../fibonacci-service/target/classes" TestClient
```

### Method 3: Using BloomRPC or Postman

1. **Import the proto file:** `proto/math.proto`
2. **Set up requests:**
   - **Factorial Service:** `localhost:9090`
   - **Fibonacci Service:** `localhost:9091`
3. **Call the `Calculate` method with:** `{"number": 5}`

## 📊 Expected Results

### Calculation Examples

| Input | Factorial Result | Fibonacci Sum Result | Combined Result |
|-------|------------------|---------------------|-----------------|
| 0     | 1                | 0                   | 1               |
| 1     | 1                | 1                   | 2               |
| 3     | 6                | 2                   | 8               |
| 5     | 120              | 7                   | 127             |

### Understanding the Results

**When you call Factorial Service with input 5:**
1. Calculates factorial(5) = 120
2. Calls Fibonacci service → gets fibonacciSum(5) = 7
3. Returns: `{"result": 120, "accumulatedResults": [127]}`

**When you call Fibonacci Service with input 5:**
1. Calculates fibonacciSum(5) = 7 (sum of first 5 Fibonacci numbers: 0+1+1+2+3)
2. Calls Factorial service → gets factorial(5) = 120
3. Returns: `{"result": 7, "accumulatedResults": [127]}`

## 🔍 Monitoring and Observability

### RabbitMQ Messages

1. **Access RabbitMQ Management UI:** http://localhost:15672
2. **Login:** guest/guest
3. **Navigate to:** Queues → results.queue
4. **Watch messages** being published every 30 seconds

### Service Logs

```bash
# View all service logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f factorial-service
docker-compose logs -f fibonacci-service
docker-compose logs -f rabbitmq
```

### Health Checks

```bash
# Check service health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health

# Check service metrics
curl http://localhost:8080/actuator/metrics
curl http://localhost:8081/actuator/metrics
```

## 🧪 Running Tests

### Run All Tests

```bash
# Windows
run-tests.bat

# Or manually:
cd factorial-service && mvn test
cd ../fibonacci-service && mvn test
```

### Test Coverage

The comprehensive test suite covers:
- ✅ **gRPC Services:** Calculation logic, error handling, edge cases
- ✅ **gRPC Clients:** Communication, error handling, timeouts
- ✅ **RabbitMQ Publishers:** Message publishing, error scenarios
- ✅ **RabbitMQ Consumers:** Message consumption, logging
- ✅ **Input Validation:** Negative numbers, overflow protection
- ✅ **Edge Cases:** Zero, one, boundary conditions

## 🔧 Configuration

### Service Ports
- **Factorial Service:** gRPC on 9090, HTTP on 8080
- **Fibonacci Service:** gRPC on 9091, HTTP on 8081
- **RabbitMQ:** 5672 (AMQP), 15672 (Management UI)

### Environment Variables
- `SPRING_RABBITMQ_HOST`: RabbitMQ host (default: localhost)
- `FACTORIAL_GRPC_HOST`: Factorial service host (default: factorial-service)
- `FIBONACCI_GRPC_HOST`: Fibonacci service host (default: fibonacci-service)

### Input Limits
- **Factorial Service:** 0-20 (prevents long overflow)
- **Fibonacci Service:** 0-92 (prevents long overflow)

## 📁 Project Structure

```
spring-grpc-microservices/
├── proto/
│   └── math.proto                 # gRPC service definitions
├── factorial-service/
│   ├── src/main/java/
│   │   └── com/example/factorial/
│   │       ├── grpc/
│   │       │   ├── FactorialGrpcService.java
│   │       │   └── FibonacciGrpcClient.java
│   │       └── messaging/
│   │           ├── ResultPublisher.java
│   │           ├── ResultConsumer.java
│   │           ├── RabbitConfig.java
│   │           └── RabbitQueueConfig.java
│   ├── src/test/java/             # Comprehensive unit tests
│   ├── Dockerfile                 # Multi-stage build
│   └── pom.xml
├── fibonacci-service/
│   ├── src/main/java/
│   │   └── com/example/fibonacci/
│   │       ├── grpc/
│   │       │   ├── FibonacciGrpcService.java
│   │       │   └── FactorialGrpcClient.java
│   │       └── messaging/
│   │           ├── ResultPublisher.java
│   │           ├── ResultConsumer.java
│   │           ├── RabbitConfig.java
│   │           └── RabbitQueueConfig.java
│   ├── src/test/java/             # Comprehensive unit tests
│   ├── Dockerfile                 # Multi-stage build
│   └── pom.xml
├── test-client/
│   └── TestClient.java            # Manual testing client
├── docker-compose.yml             # Complete orchestration
├── run-tests.bat                  # Test runner script
├── README.md                      # This file
└── TESTING_GUIDE.md              # Detailed testing guide
```

## 🐛 Troubleshooting

### Common Issues

1. **Services won't start:**
   ```bash
   # Check if ports are available
   netstat -an | findstr "9090\|9091\|8080\|8081"
   
   # Clean restart
   docker-compose down
   docker-compose up --build
   ```

2. **gRPC connection errors:**
   - Ensure services are fully started (check health endpoints)
   - Verify Docker Compose logs for errors
   - Check service discovery (hostnames in docker-compose.yml)

3. **RabbitMQ connection issues:**
   - Check RabbitMQ is running: http://localhost:15672
   - Verify network connectivity between services
   - Check credentials: guest/guest

4. **Build failures:**
   - Ensure Docker has enough memory (4GB+ recommended)
   - Check internet connection for Maven dependencies
   - Clear Docker cache: `docker system prune`

### Debug Commands

```bash
# Check service status
docker-compose ps

# View detailed logs
docker-compose logs -f [service-name]

# Restart specific service
docker-compose restart [service-name]

# Clean everything and restart
docker-compose down -v
docker-compose up --build
```

## 🎯 Key Learning Points

### gRPC Benefits
- **Protocol Buffers** - Efficient binary serialization
- **Type Safety** - Compile-time service contracts
- **High Performance** - HTTP/2, streaming, multiplexing
- **Code Generation** - Automatic client/server code

### RabbitMQ Benefits
- **Asynchronous Communication** - Decoupled services
- **Message Persistence** - Reliable message delivery
- **Routing Flexibility** - Topic exchanges, routing keys
- **Scalability** - Horizontal scaling, load balancing

### Microservices Benefits
- **Service Independence** - Independent deployment, scaling
- **Technology Diversity** - Different languages, frameworks
- **Fault Isolation** - Single service failure doesn't break all
- **Team Autonomy** - Independent development teams

## 🚀 Production Considerations

This demo includes production-ready features:
- ✅ **Health checks** for service monitoring
- ✅ **Error handling** and input validation
- ✅ **Logging** for observability
- ✅ **Security** (non-root containers)
- ✅ **Resource management** (proper cleanup)
- ✅ **Overflow protection** for calculations
- ✅ **Comprehensive testing** coverage

## 🎓 Next Steps

Once you understand this demo, explore:
- **Service Mesh** (Istio/Linkerd) for advanced networking
- **API Gateway** (Kong/Ambassador) for external access
- **Monitoring** (Prometheus/Grafana) for metrics
- **Distributed Tracing** (Jaeger/Zipkin) for debugging
- **Circuit Breakers** (Resilience4j) for fault tolerance
- **Load Balancing** and **Auto-scaling**

## 📝 License

This is a learning demo. Feel free to use and modify for educational purposes.

---

**Happy Learning! 🚀**

For detailed testing instructions, see [TESTING_GUIDE.md](TESTING_GUIDE.md) 