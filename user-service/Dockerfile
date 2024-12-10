FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

