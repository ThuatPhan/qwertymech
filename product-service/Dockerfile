# The jar is built by Maven on the host/CI (mvn clean install from the repo root),
# this image only packages it.
FROM amazoncorretto:21.0.4

# Install curl for container healthcheck
RUN yum install -y curl

WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
