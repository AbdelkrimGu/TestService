# Use a base image with Java and Maven pre-installed
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and project files
COPY pom.xml .
COPY src ./src

# Build the project
RUN mvn package -DskipTests

# Use a lightweight base image for the final image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/UserService-0.0.1-SNAPSHOT.jar ./your-app.jar

# Expose the port your Spring Boot app listens on (if necessary)
EXPOSE 8080

# Set the command to run your application
CMD ["java", "-jar", "your-app.jar"]
