FROM docker.io/amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/file-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
