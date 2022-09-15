FROM maven:3.6.0-jdk-11-slim AS build

COPY src /home/app/src
COPY pom.xml /home/app

WORKDIR /home/app

RUN mvn -f . clean package




FROM openjdk:11-jre-slim

RUN mkdir /app

COPY --from=build /home/app/target/*.jar /app/applicationB.jar

ENTRYPOINT ["java","-jar","/app/applicationB.jar"]