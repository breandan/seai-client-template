FROM java:8

COPY . /project
WORKDIR /project

EXPOSE 8082/tcp

RUN ./gradlew run