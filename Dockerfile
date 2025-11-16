# --- STAGE 1: Build the JAR ---
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -e -DskipTests package

# --- STAGE 2: Run the bot ---
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV BOT_TOKEN="8402794640:AAFVC0kNprTIpNdk0T_wrjsVHMSqbz3FsL8"
ENV BOT_USERNAME="AsistenteEnergyBot"

CMD ["sh", "-c", "java -jar app.jar"]
