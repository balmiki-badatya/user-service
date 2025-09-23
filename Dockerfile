FROM amazoncorretto:21.0.8-al2

#Set working directory
WORKDIR /app

#Copy files to /app
COPY . .

# build the application
COPY build/libs/user-service-0.0.1-SNAPSHOT.jar ./user-service.jar

# Expose port to host
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]