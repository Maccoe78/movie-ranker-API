# STAGE 1: BUILD
# Gebruik Java 24 image
FROM eclipse-temurin:24-jdk-alpine AS builder

# Werk directory
WORKDIR /app

# Copy Maven wrapper en pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (dit wordt gecached!)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build de applicatie
RUN ./mvnw clean package -DskipTests

# STAGE 2: RUNTIME
# Gebruik kleinere runtime image (geen build tools nodig)
FROM eclipse-temurin:24-jre-alpine

WORKDIR /app

# Copy JAR van builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check (optioneel)
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run applicatie
ENTRYPOINT ["java", "-jar", "app.jar"]