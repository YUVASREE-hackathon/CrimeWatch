FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace
COPY pom.xml .
COPY src src
RUN mvn -B -DskipTests --no-transfer-progress package

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S crimewatch && adduser -S crimewatch -G crimewatch
WORKDIR /app
COPY --from=build /workspace/target/crimewatch-api.jar app.jar
RUN mkdir -p /app/uploads && chown -R crimewatch:crimewatch /app
USER crimewatch
EXPOSE 8080
HEALTHCHECK --interval=20s --timeout=5s --start-period=40s --retries=5 CMD wget -q -O - http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]

