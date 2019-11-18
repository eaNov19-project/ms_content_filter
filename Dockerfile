FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY target/ms_content_filter.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
