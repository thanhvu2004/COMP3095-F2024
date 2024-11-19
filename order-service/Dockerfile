FROM gradle:8-jdk23-alpine as builder
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:23-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/order-service.jar

EXPOSE 8082

ENV POSTGRES_DB_USERNAME=admin \
    POSTGRES_DB_PASSWORD=password

ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]