# modules/world/Dockerfile — build du module 'world' depuis la racine du monorepo
FROM maven:3.9-eclipse-temurin-23 AS build
WORKDIR /src
COPY . .
WORKDIR /src/apps/wiki
RUN mvn -q -DskipTests package

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /src/apps/wiki/target/*.jar /app/app.jar
ENV JAVA_OPTS=""
EXPOSE 8082
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]