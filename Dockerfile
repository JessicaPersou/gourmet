FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy the pom.xml file
COPY pom.xml .
# Copy the src directory
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw .
COPY mvnw.cmd .

# Build the application
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Application port
EXPOSE 8080

# Entry point
ENTRYPOINT ["java", "-jar", "app.jar"]