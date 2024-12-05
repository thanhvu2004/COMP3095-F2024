FROM gradle:8-jdk23-alpine as builder
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:23-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/notification-service.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/notification-service.jar"]