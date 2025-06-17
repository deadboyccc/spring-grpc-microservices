# Multi-stage build for better efficiency
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

# Copy proto files first for better caching
COPY proto/ /proto/

# Copy pom.xml and source code
COPY pom.xml .
COPY src/ ./src/

# Build the application
RUN mvn clean package -DskipTests

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