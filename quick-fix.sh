#!/bin/bash

echo "========================================"
echo "Quick Fix for Network Issues"
echo "========================================"
echo ""

echo "1. Clearing Docker cache..."
docker system prune -f

echo ""
echo "2. Pulling fresh Maven image..."
docker pull maven:3.9.5-eclipse-temurin-17-alpine

echo ""
echo "3. Testing network connectivity..."
if curl -s --connect-timeout 10 https://repo1.maven.org/maven2 > /dev/null; then
    echo "   ✓ Network is working"
else
    echo "   ✗ Network issues detected"
    echo "   Trying alternative approach..."
    
    echo ""
    echo "4. Building with no cache and longer timeout..."
    DOCKER_BUILDKIT=1 docker-compose build --no-cache --progress=plain
fi

echo ""
echo "5. Starting services..."
docker-compose up --build

echo ""
echo "If you're still having issues, run: ./troubleshoot-network.sh" 