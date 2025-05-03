# Build Stage
FROM maven:3.9-amazoncorretto-21-debian AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Run Stage
FROM openjdk:21

WORKDIR /app

COPY --from=builder /app/target/sporty-0.0.1-SNAPSHOT.jar sporty.jar

ENTRYPOINT ["java", "-jar", "sporty.jar"]
