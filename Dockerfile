FROM openjdk:8-jdk-alpine
MAINTAINER jp.co.axa
COPY build/libs/api-demo-**-SNAPSHOT.jar employeeApi.jar
ENTRYPOINT ["java","-jar","/employeeApi.jar"]