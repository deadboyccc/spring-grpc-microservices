#!/bin/bash

echo "========================================"
echo "Network Troubleshooting Script"
echo "========================================"
echo ""

# Check Docker
echo "1. Checking Docker..."
if docker info > /dev/null 2>&1; then
    echo "   ✓ Docker is running"
else
    echo "   ✗ Docker is not running. Please start Docker Desktop."
    exit 1
fi

# Check DNS
echo ""
echo "2. Checking DNS resolution..."
if nslookup repo.maven.apache.org > /dev/null 2>&1; then
    echo "   ✓ DNS resolution working"
else
    echo "   ✗ DNS resolution failed for repo.maven.apache.org"
    echo "   Try: echo '8.8.8.8' >> /etc/resolv.conf"
fi

# Check connectivity to Maven Central
echo ""
echo "3. Checking Maven Central connectivity..."
if curl -s --connect-timeout 10 https://repo.maven.apache.org/maven2 > /dev/null; then
    echo "   ✓ Maven Central accessible"
else
    echo "   ✗ Cannot access Maven Central"
    echo "   Trying alternative mirrors..."
    
    if curl -s --connect-timeout 10 https://repo1.maven.org/maven2 > /dev/null; then
        echo "   ✓ repo1.maven.org accessible"
    else
        echo "   ✗ repo1.maven.org also failed"
    fi
    
    if curl -s --connect-timeout 10 https://maven.aliyun.com/repository/public > /dev/null; then
        echo "   ✓ Aliyun mirror accessible"
    else
        echo "   ✗ Aliyun mirror also failed"
    fi
fi

# Check Docker network
echo ""
echo "4. Checking Docker network..."
if docker network ls | grep -q "bridge"; then
    echo "   ✓ Docker bridge network exists"
else
    echo "   ✗ Docker bridge network missing"
fi

# Check Docker daemon
echo ""
echo "5. Checking Docker daemon..."
if docker version > /dev/null 2>&1; then
    echo "   ✓ Docker daemon responding"
else
    echo "   ✗ Docker daemon not responding"
fi

# Check available ports
echo ""
echo "6. Checking required ports..."
ports=(5672 15672 8080 8081 9090 9091)
for port in "${ports[@]}"; do
    if netstat -an | grep -q ":$port "; then
        echo "   ⚠ Port $port is already in use"
    else
        echo "   ✓ Port $port is available"
    fi
done

echo ""
echo "========================================"
echo "Troubleshooting Complete"
echo "========================================"
echo ""
echo "If you're still having issues:"
echo "1. Try using a VPN if you're behind a corporate firewall"
echo "2. Check your firewall settings"
echo "3. Try: docker system prune -a"
echo "4. Try: docker-compose build --no-cache"
echo "5. Check Docker Desktop settings"
echo "" 