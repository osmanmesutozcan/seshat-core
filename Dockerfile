FROM maven:3.6.0-jdk-11-slim AS build

ARG POSTGRES_URL
ENV POSTGRES_URL ${POSTGRES_URL}

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

COPY /home/app/target/slackapp-0.0.1-SNAPSHOT.jar /usr/local/lib/slackapp.jar
EXPOSE 8080
