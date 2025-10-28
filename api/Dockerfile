FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn install:install-file -Dfile='src/main/libs/logger-0.0.1-SNAPSHOT.jar' -DgroupId='fr.phenix333' -DartifactId='logger' -Dversion='0.0.1-SNAPSHOT' -Dpackaging=jar

RUN mvn clean install -DskipTests

FROM openjdk:26-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y python3 python3-pip && rm -rf /var/lib/apt/lists/*

RUN mkdir Logs
RUN chmod 777 Logs
RUN mkdir /root/bots
RUN chmod 777 /root/bots

COPY --from=build /app/target/awale-arena-0.0.1-SNAPSHOT.jar .

ENV SUPABASE_USER=""
ENV SUPABASE_PASSWORD=""

CMD ["java", "-jar", "awale-arena-0.0.1-SNAPSHOT.jar", "--supabase.user=${SUPABASE_USER}", "--supabase.password=${SUPABASE_PASSWORD}"]
