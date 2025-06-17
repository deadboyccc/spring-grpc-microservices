@echo off
echo ========================================
echo Starting gRPC + RabbitMQ Demo
echo ========================================

echo.
echo This will start:
echo - RabbitMQ (with management UI)
echo - Factorial Service (gRPC + HTTP)
echo - Fibonacci Service (gRPC + HTTP)
echo.

REM Check if Docker is running
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Docker is not running. Please start Docker and try again.
    pause
    exit /b 1
)

REM Check network connectivity
echo Checking network connectivity...
curl -s --connect-timeout 5 https://repo1.maven.org/maven2 >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo WARNING: Network connectivity issues detected. This may cause build failures.
    echo If the build fails, try:
    echo 1. Check your internet connection
    echo 2. Try using a VPN if you're behind a corporate firewall
    echo 3. Wait a few minutes and try again
    echo.
    pause
)

echo Starting services...
echo Note: First build may take several minutes due to Maven dependency downloads.
echo.

REM Build with retry logic
set max_retries=3
set retry_count=0

:retry_loop
set /a retry_count+=1
echo Attempt %retry_count% of %max_retries%...

docker-compose up --build
if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================
    echo Demo is running successfully!
    echo ========================================
    echo.
    echo Access points:
    echo - RabbitMQ Management: http://localhost:15672 (guest/guest)
    echo - Factorial Service:    http://localhost:8080/actuator/health
    echo - Fibonacci Service:    http://localhost:8081/actuator/health
    echo.
    echo Test with grpcurl:
    echo   grpcurl -plaintext -d '{"number": 5}' localhost:9090 math.FactorialService/Calculate
    echo   grpcurl -plaintext -d '{"number": 5}' localhost:9091 math.FibonacciService/Calculate
    echo.
    pause
    exit /b 0
) else (
    if %retry_count% lss %max_retries% (
        echo Build failed. Retrying in 30 seconds...
        timeout /t 30 /nobreak >nul
        goto retry_loop
    )
)

echo.
echo ========================================
echo Build failed after %max_retries% attempts
echo ========================================
echo.
echo Troubleshooting steps:
echo 1. Check your internet connection
echo 2. Try using a VPN if you're behind a corporate firewall
echo 3. Clear Docker cache: docker system prune -a
echo 4. Try building manually: docker-compose build --no-cache
echo 5. Check Docker logs for specific errors
echo.
pause 