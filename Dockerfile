# Step 1: Use Maven to build the project
FROM maven:3.8.4-openjdk-17 AS build

# Set working directory inside container
WORKDIR /app

# Copy pom.xml and download dependencies (for faster rebuilds)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Package the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Step 2: Run the application using a lightweight JDK image
FROM openjdk:17-jdk-slim

# Set working directory for the app
WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /app/target/ContactManager-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
