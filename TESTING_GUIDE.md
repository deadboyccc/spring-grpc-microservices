# Testing Guide for gRPC + RabbitMQ Demo

This guide provides step-by-step instructions for testing the microservices demo.

## 🚀 Quick Start Testing

### 1. Start the Services

```bash
# Navigate to the project directory
cd spring-grpc-microservices

# Start all services with Docker Compose
docker-compose up --build
```

Wait for all services to start (you should see logs from all three services: rabbitmq, factorial-service, fibonacci-service).

### 2. Verify Services are Running

Check these URLs in your browser:

- **RabbitMQ Management UI:** http://localhost:15672
  - Username: `guest`
  - Password: `guest`
  - You should see the `results.queue` and `results.exchange`

- **Factorial Service Health:** http://localhost:8080/actuator/health
  - Should return: `{"status":"UP"}`

- **Fibonacci Service Health:** http://localhost:8081/actuator/health
  - Should return: `{"status":"UP"}`

## 🧪 Testing Methods

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

#### Test Factorial Service

```bash
# Test with number 5
grpcurl -plaintext -d '{"number": 5}' localhost:9090 math.FactorialService/Calculate

# Expected response:
# {
#   "result": 120,
#   "accumulatedResults": [125]
# }
```

#### Test Fibonacci Service

```bash
# Test with number 5
grpcurl -plaintext -d '{"number": 5}' localhost:9091 math.FibonacciService/Calculate

# Expected response:
# {
#   "result": 7,
#   "accumulatedResults": [127]
# }
```

### Method 2: Using the Test Client

1. **Compile the test client:**
   ```bash
   cd test-client
   javac -cp ".:../factorial-service/target/classes:../fibonacci-service/target/classes" TestClient.java
   ```

2. **Run the test client:**
   ```bash
   java -cp ".:../factorial-service/target/classes:../fibonacci-service/target/classes" TestClient
   ```

### Method 3: Using BloomRPC or Postman

1. **Import the proto file:** `proto/math.proto`
2. **Set up requests:**
   - **Factorial Service:** `localhost:9090`
   - **Fibonacci Service:** `localhost:9091`
3. **Call the `Calculate` method with:** `{"number": 5}`

## 📊 Expected Results

### Test Cases

| Input | Factorial Result | Fibonacci Sum Result | Combined Result |
|-------|------------------|---------------------|-----------------|
| 0     | 1                | 0                   | 1               |
| 1     | 1                | 1                   | 2               |
| 3     | 6                | 2                   | 8               |
| 5     | 120              | 7                   | 127             |

### Understanding the Results

**Factorial Service:**
- Calculates factorial of input number
- Calls Fibonacci service to get Fibonacci sum
- Returns: `factorial + fibonacci_sum`

**Fibonacci Service:**
- Calculates sum of first N Fibonacci numbers
- Calls Factorial service to get factorial
- Returns: `fibonacci_sum + factorial`

## 🔍 Monitoring RabbitMQ Messages

### 1. Check RabbitMQ Management UI

1. Go to: http://localhost:15672
2. Login with: `guest` / `guest`
3. Navigate to: **Queues** → **results.queue**
4. You should see messages being published every 30 seconds

### 2. Check Service Logs

```bash
# View factorial service logs
docker-compose logs factorial-service

# View fibonacci service logs
docker-compose logs fibonacci-service

# View all logs
docker-compose logs -f
```

You should see logs like:
```
[FactorialService] Published accumulated results: [125, 127, ...]
[FibonacciService] Published accumulated results: [127, 129, ...]
```

## 🧪 Running Unit Tests

### Run All Tests

```bash
# Windows
run-tests.bat

# Or manually:
cd factorial-service && mvn test
cd ../fibonacci-service && mvn test
```

### Test Coverage

The tests cover:
- ✅ **gRPC Services:** Calculation logic and response building
- ✅ **gRPC Clients:** Client communication (mocked)
- ✅ **RabbitMQ Publishers:** Message publishing
- ✅ **RabbitMQ Consumers:** Message consumption
- ✅ **Error Handling:** Negative number validation
- ✅ **Edge Cases:** Zero, one, and boundary conditions

## 🐛 Troubleshooting

### Common Issues

1. **Services won't start:**
   ```bash
   # Check if ports are available
   netstat -an | findstr "9090\|9091\|8080\|8081"
   
   # Stop any conflicting services
   docker-compose down
   ```

2. **gRPC connection errors:**
   - Ensure services are fully started
   - Check Docker Compose logs
   - Verify port mappings in docker-compose.yml

3. **RabbitMQ connection issues:**
   - Check RabbitMQ is running: http://localhost:15672
   - Verify network connectivity between services

4. **Test failures:**
   - Ensure Maven dependencies are downloaded
   - Check Java version (requires Java 17+)
   - Run `mvn clean test` to clear cached tests

### Debug Commands

```bash
# Check service status
docker-compose ps

# View detailed logs
docker-compose logs -f [service-name]

# Restart services
docker-compose restart

# Clean restart
docker-compose down
docker-compose up --build
```

## 📈 Performance Testing

### Load Testing with grpcurl

```bash
# Test multiple concurrent requests
for i in {1..10}; do
  grpcurl -plaintext -d '{"number": 5}' localhost:9090 math.FactorialService/Calculate &
done
wait
```

### Monitor Performance

1. **Check service metrics:** http://localhost:8080/actuator/metrics
2. **Monitor RabbitMQ throughput:** http://localhost:15672
3. **Watch service logs** for response times

## ✅ Success Criteria

Your demo is working correctly if:

1. ✅ **Services start without errors**
2. ✅ **Health checks return "UP"**
3. ✅ **gRPC calls return expected results**
4. ✅ **RabbitMQ messages are published every 30 seconds**
5. ✅ **All unit tests pass**
6. ✅ **Services can communicate with each other**

## 🎯 Next Steps

Once basic testing is complete, you can:

1. **Add more test cases** with different numbers
2. **Test error scenarios** (negative numbers, large numbers)
3. **Monitor performance** under load
4. **Extend the demo** with additional services
5. **Add integration tests** for end-to-end scenarios

## 📞 Getting Help

If you encounter issues:

1. **Check the logs:** `docker-compose logs`
2. **Verify configuration:** Check docker-compose.yml and application.yml
3. **Test individual components:** Use the health endpoints
4. **Review the README:** For architecture and setup details

Happy testing! 🚀 