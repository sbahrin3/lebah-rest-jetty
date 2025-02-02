# Use an official OpenJDK runtime as a base image
FROM eclipse-temurin:21-jdk-alpine


# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file to the container
COPY target/rest.qard-0.0.1-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

# Expose the port
EXPOSE 8080

# Define the command to run the app
CMD ["java", "-jar", "app.jar"]
