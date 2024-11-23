#this dockerfile only manages the server.
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY server/build/libs/server-all.jar server.jar
COPY .env .env
COPY server/src/main/resources/ktor-firebase-auth-firebase-adminsdk.json ktor-firebase-auth-firebase-adminsdk.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]