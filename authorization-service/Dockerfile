FROM amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/authorization-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
