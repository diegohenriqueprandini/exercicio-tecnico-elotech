FROM azul/zulu-openjdk-alpine:17.0.4.1

ENV PROJECT_NAME=exercicio-tecnico-elotech
ENV ARTEFACT=${PROJECT_NAME}*.jar

EXPOSE 8080

COPY ../target/${ARTEFACT} /app/

WORKDIR /app

ENTRYPOINT java -jar $ARTEFACT
