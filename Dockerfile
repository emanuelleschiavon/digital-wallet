FROM openjdk:21-jdk
COPY build/libs/wallet-0.0.1-SNAPSHOT.jar app.jar
CMD ["sh","-c","java -jar app.jar"]
