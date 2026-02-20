FROM openjdk:8-jdk-slim
WORKDIR /app
COPY target/dsr-0.0.1-SNAPSHOT.jar /app/dsr-0.0.1-SNAPSHOT.jar
EXPOSE 8585
ENTRYPOINT ["java","-jar","dsr-0.0.1-SNAPSHOT.jar"]