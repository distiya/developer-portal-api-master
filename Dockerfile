FROM openjdk:11

WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn
RUN ./mvnw dependency:go-offline

COPY src /app/src
RUN ./mvnw package

ENTRYPOINT ["java", "-jar", "/app/target/developer-portal-api-0.0.1-SNAPSHOT.jar"]
