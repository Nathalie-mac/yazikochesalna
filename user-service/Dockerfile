FROM docker.io/amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/user-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
