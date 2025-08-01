# Use OpenJDK base image
FROM eclipse-temurin:19-jdk

# Set working directory
WORKDIR /app

# Copy the jar file (you may change the name if different)
COPY target/Zippy-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
