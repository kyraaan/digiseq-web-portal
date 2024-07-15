FROM amazoncorretto:21-al2023-jdk

WORKDIR /app

COPY target/digiseq-web-portal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]
