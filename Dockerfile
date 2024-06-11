FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/mail-sender-0.0.1-SNAPSHOT.jar mail-sender.jar
ENTRYPOINT ["java","-jar","/mail-sender.jar"]
