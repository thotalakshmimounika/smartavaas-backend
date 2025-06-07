# -------- Stage 1: Build the JAR --------
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy full source and build
COPY src ./src
RUN mvn clean package -DskipTests

# -------- Stage 2: Run the JAR --------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
