FROM amazonlinux:2

#Set working directory
WORKDIR /app

#Copy files to /app
cp . /app

#Install JAVA
RUN yum install java-21-amazon-corretto-devel

# build the application
RUN ./gradlew clean build

# Expose port to host
EXPOSE 9090

# Run the application
CMD["./gradlew","bootRun"]