FROM amazoncorretto:21.0.4

RUN yum install -y curl

WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
