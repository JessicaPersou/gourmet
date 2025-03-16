FROM openjdk:21-jdk-slim

WORKDIR /app

LABEL authors="Jessica Persou"

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
