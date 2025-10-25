# Use an official OpenJDK image as the base
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven/Gradle build files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

# Build the project using Maven (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Copy the built JAR to a standard location
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the JAR
ENTRYPOINT ["java","-jar","/app/app.jar"]
