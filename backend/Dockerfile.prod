# Stage 1: Build the Spring Boot JAR
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app

COPY . .
RUN ./gradlew bootJar --no-daemon

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
