# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build


# Copy the entire project to the container
COPY . .

# Build the Spring Boot application (skip tests for faster build)
RUN mvn package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim

# Copy the built JAR from the build stage
COPY --from=build /pdf-compress/target/pdf-compress*.jar app.jar

# Expose application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
