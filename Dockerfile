FROM amazonlinux:2

#Set working directory
WORKDIR /app

#Copy files to /app
COPY . /app

#Install JAVA
RUN yum update -y && \
    yum install -y curl && \
    curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo && \
    rpm --import https://yum.corretto.aws/corretto.key && \
    yum install -y java-21-amazon-corretto-devel && \
    yum clean all

# build the application
COPY build/libs/user-service-0.0.1-SNAPSHOT.jar /app/user-service.war
# Expose port to host
EXPOSE 9090

ENTRYPOINT ["java"]
# Run the application
CMD ["-jar", "/app/user-service.jar"]