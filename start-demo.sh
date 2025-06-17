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

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check network connectivity
echo "Checking network connectivity..."
if ! curl -s --connect-timeout 5 https://repo1.maven.org/maven2 > /dev/null; then
    echo "WARNING: Network connectivity issues detected. This may cause build failures."
    echo "If the build fails, try:"
    echo "1. Check your internet connection"
    echo "2. Try using a VPN if you're behind a corporate firewall"
    echo "3. Wait a few minutes and try again"
    echo ""
    read -p "Press Enter to continue anyway..."
fi

echo "Starting services..."
echo "Note: First build may take several minutes due to Maven dependency downloads."
echo ""

# Build with retry logic
max_retries=3
retry_count=0

while [ $retry_count -lt $max_retries ]; do
    echo "Attempt $((retry_count + 1)) of $max_retries..."
    
    if docker-compose up --build; then
        echo ""
        echo "========================================"
        echo "Demo is running successfully!"
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
        exit 0
    else
        retry_count=$((retry_count + 1))
        if [ $retry_count -lt $max_retries ]; then
            echo "Build failed. Retrying in 30 seconds..."
            sleep 30
        fi
    fi
done

echo ""
echo "========================================"
echo "Build failed after $max_retries attempts"
echo "========================================"
echo ""
echo "Troubleshooting steps:"
echo "1. Check your internet connection"
echo "2. Try using a VPN if you're behind a corporate firewall"
echo "3. Clear Docker cache: docker system prune -a"
echo "4. Try building manually: docker-compose build --no-cache"
echo "5. Check Docker logs for specific errors"
echo "" 