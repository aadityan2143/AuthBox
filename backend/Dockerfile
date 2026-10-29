# ---- Stage 1: build the JAR ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy pom first so dependency downloads are cached unless pom.xml changes
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Stage 2: slim runtime image ----
FROM eclipse-temurin:21-jre
WORKDIR /app
# Run as a non-root user
RUN useradd -r -u 1001 appuser
COPY --from=build /app/target/*.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
