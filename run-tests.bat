@echo off
echo ========================================
echo Running gRPC + RabbitMQ Demo Tests
echo ========================================

echo.
echo 1. Testing Factorial Service...
cd factorial-service
call mvn test
if %ERRORLEVEL% neq 0 (
    echo ERROR: Factorial service tests failed!
    exit /b 1
)
cd ..

echo.
echo 2. Testing Fibonacci Service...
cd fibonacci-service
call mvn test
if %ERRORLEVEL% neq 0 (
    echo ERROR: Fibonacci service tests failed!
    exit /b 1
)
cd ..

echo.
echo ========================================
echo All tests completed successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Start the services: docker-compose up --build
echo 2. Test manually with grpcurl or the TestClient
echo 3. Check RabbitMQ messages at http://localhost:15672
echo.
pause 