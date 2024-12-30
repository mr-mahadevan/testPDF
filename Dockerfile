# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /pdf-compress

# Copy the pom.xml file and download project dependencies
COPY pdf-compress/pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project to the container
COPY pdf-compress .

# Build the Spring Boot application (skip tests for faster build)
RUN mvn package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim

# Set the working directory for the runtime stage
WORKDIR /pdf-compress

# Copy the built JAR from the build stage
COPY --from=build /pdf-compress/target/pdf-compress*.jar app.jar

# Expose application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
