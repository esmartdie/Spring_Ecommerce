FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/ecommerce-0.0.1-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/templates /app/templates
COPY src/main/resources/static /app/static
COPY images /app/images
ENTRYPOINT ["java", "-Dspring.thymeleaf.prefix=file:/app/templates/", "-Dspring.resources.static-locations=file:/app/static/", "-jar", "/app/app.jar"]

