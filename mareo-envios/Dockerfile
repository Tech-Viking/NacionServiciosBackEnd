# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/mareo-envios-*.jar mareo-envios.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/mareo-envios.jar"]