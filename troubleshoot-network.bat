@echo off
echo ========================================
echo Network Troubleshooting Script
echo ========================================
echo.

REM Check Docker
echo 1. Checking Docker...
docker info >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo    ✓ Docker is running
) else (
    echo    ✗ Docker is not running. Please start Docker Desktop.
    pause
    exit /b 1
)

REM Check DNS
echo.
echo 2. Checking DNS resolution...
nslookup repo.maven.apache.org >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo    ✓ DNS resolution working
) else (
    echo    ✗ DNS resolution failed for repo.maven.apache.org
    echo    Try: echo 8.8.8.8 ^>^> C:\Windows\System32\drivers\etc\hosts
)

REM Check connectivity to Maven Central
echo.
echo 3. Checking Maven Central connectivity...
curl -s --connect-timeout 10 https://repo.maven.apache.org/maven2 >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo    ✓ Maven Central accessible
) else (
    echo    ✗ Cannot access Maven Central
    echo    Trying alternative mirrors...
    
    curl -s --connect-timeout 10 https://repo1.maven.org/maven2 >nul 2>&1
    if %ERRORLEVEL% equ 0 (
        echo    ✓ repo1.maven.org accessible
    ) else (
        echo    ✗ repo1.maven.org also failed
    )
    
    curl -s --connect-timeout 10 https://maven.aliyun.com/repository/public >nul 2>&1
    if %ERRORLEVEL% equ 0 (
        echo    ✓ Aliyun mirror accessible
    ) else (
        echo    ✗ Aliyun mirror also failed
    )
)

REM Check Docker network
echo.
echo 4. Checking Docker network...
docker network ls | findstr "bridge" >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo    ✓ Docker bridge network exists
) else (
    echo    ✗ Docker bridge network missing
)

REM Check Docker daemon
echo.
echo 5. Checking Docker daemon...
docker version >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo    ✓ Docker daemon responding
) else (
    echo    ✗ Docker daemon not responding
)

REM Check available ports
echo.
echo 6. Checking required ports...
for %%p in (5672 15672 8080 8081 9090 9091) do (
    netstat -an | findstr ":%%p " >nul 2>&1
    if !ERRORLEVEL! equ 0 (
        echo    ⚠ Port %%p is already in use
    ) else (
        echo    ✓ Port %%p is available
    )
)

echo.
echo ========================================
echo Troubleshooting Complete
echo ========================================
echo.
echo If you're still having issues:
echo 1. Try using a VPN if you're behind a corporate firewall
echo 2. Check your firewall settings
echo 3. Try: docker system prune -a
echo 4. Try: docker-compose build --no-cache
echo 5. Check Docker Desktop settings
echo.
pause 