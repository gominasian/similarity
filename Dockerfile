FROM sbt:latest

WORKDIR /app

COPY project/ project/
COPY build.sbt .
COPY src/ src/

RUN sbt compile

EXPOSE 9000

CMD ["sbt", "run"]