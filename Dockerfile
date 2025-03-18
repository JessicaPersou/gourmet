#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /app
#
## Copy the pom.xml file
#COPY pom.xml .
## Copy the src directory
#COPY src ./src
#
## Build the application
#RUN mvn clean package -DskipTests
#
## Run stage
#FROM openjdk:21-jdk-slim
#WORKDIR /app
#
## Copy the built JAR file from the build stage
#COPY --from=build /app/target/*.jar app.jar
#
## Application port
#EXPOSE 8080
#
## Entry point
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:21-jdk-slim

WORKDIR /app

LABEL authors="Jessica Persou"

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
