FROM hseeberger/scala-sbt:11.0.11_1.5.5_2.13.6 as builder

WORKDIR /app

COPY build.sbt ./
COPY project ./project

RUN sbt update

COPY . .
RUN sbt assembly

FROM openjdk:11-jre-slim

WORKDIR /app
COPY --from=builder /app/target/scala-2.13/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
