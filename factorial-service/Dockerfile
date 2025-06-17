# Multi-stage build for better efficiency
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

# Add Maven settings for better network reliability
RUN echo '<?xml version="1.0" encoding="UTF-8"?>' > /usr/share/maven/ref/settings.xml && \
  echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' >> /usr/share/maven/ref/settings.xml && \
  echo '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> /usr/share/maven/ref/settings.xml && \
  echo '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0' >> /usr/share/maven/ref/settings.xml && \
  echo '                          https://maven.apache.org/xsd/settings-1.0.0.xsd">' >> /usr/share/maven/ref/settings.xml && \
  echo '  <mirrors>' >> /usr/share/maven/ref/settings.xml && \
  echo '    <mirror>' >> /usr/share/maven/ref/settings.xml && \
  echo '      <id>central-mirror</id>' >> /usr/share/maven/ref/settings.xml && \
  echo '      <name>Central Repository Mirror</name>' >> /usr/share/maven/ref/settings.xml && \
  echo '      <url>https://repo1.maven.org/maven2</url>' >> /usr/share/maven/ref/settings.xml && \
  echo '      <mirrorOf>central</mirrorOf>' >> /usr/share/maven/ref/settings.xml && \
  echo '    </mirror>' >> /usr/share/maven/ref/settings.xml && \
  echo '  </mirrors>' >> /usr/share/maven/ref/settings.xml && \
  echo '</settings>' >> /usr/share/maven/ref/settings.xml

# Copy pom.xml and source code
COPY factorial-service/pom.xml .
COPY factorial-service/src/ ./src/

# Build the application with retry logic for network issues
RUN mvn clean package -DskipTests --settings /usr/share/maven/ref/settings.xml || \
  (sleep 10 && mvn clean package -DskipTests --settings /usr/share/maven/ref/settings.xml) || \
  (sleep 20 && mvn clean package -DskipTests --settings /usr/share/maven/ref/settings.xml)

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user for security
RUN addgroup -g 1001 -S appgroup && \
  adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build --chown=appuser:appgroup target/factorial-service-0.0.1-SNAPSHOT.jar app.jar

# Switch to non-root user
USER appuser

EXPOSE 8080 9090

ENTRYPOINT ["java", "-jar", "/app/app.jar"] 