# Dockerfile for SpringBoot Application image

FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 8888

ARG JAR_FILE=/target/*.jar

COPY ${JAR_FILE} cicd.jar

RUN echo "I am creating a docker image for my spring boot application"

MAINTAINER "mechashishsingh@gmail.com"

ENTRYPOINT ["java", "-jar", "cicd.jar"]