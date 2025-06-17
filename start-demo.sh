#!/bin/bash

echo "========================================"
echo "Starting gRPC + RabbitMQ Demo"
echo "========================================"

echo ""
echo "This will start:"
echo "- RabbitMQ (with management UI)"
echo "- Factorial Service (gRPC + HTTP)"
echo "- Fibonacci Service (gRPC + HTTP)"
echo ""

echo "Starting services..."
docker-compose up --build

echo ""
echo "========================================"
echo "Demo is running!"
echo "========================================"
echo ""
echo "Access points:"
echo "- RabbitMQ Management: http://localhost:15672 (guest/guest)"
echo "- Factorial Service:    http://localhost:8080/actuator/health"
echo "- Fibonacci Service:    http://localhost:8081/actuator/health"
echo ""
echo "Test with grpcurl:"
echo "  grpcurl -plaintext -d '{\"number\": 5}' localhost:9090 math.FactorialService/Calculate"
echo "  grpcurl -plaintext -d '{\"number\": 5}' localhost:9091 math.FibonacciService/Calculate"
echo "" 