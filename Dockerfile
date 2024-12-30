# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build

COPY . .
# Copy the pom.xml file and download project dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project to the container
COPY . .

# Build the Spring Boot application (skip tests for faster build)
RUN mvn clean install package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim


# Copy the built JAR from the build stage
COPY --from=build /target/app-1.jar .

# Expose application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
