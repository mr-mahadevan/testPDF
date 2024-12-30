# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy only the pom.xml file and download project dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy the entire project to the working directory
COPY . ./

# Build the Spring Boot application (skip tests for faster build)
RUN mvn clean install -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
