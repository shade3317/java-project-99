FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.7

RUN apt-get update && apt-get install -yq make unzip

WORKDIR /backend

COPY ./ .

RUN ./gradlew --no-daemon build

EXPOSE 8080

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar