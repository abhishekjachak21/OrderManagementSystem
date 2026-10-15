
# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Gradle wrapper and project files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build executable Spring Boot JAR
RUN chmod +x gradlew && \
    ./gradlew bootJar --no-daemon && \
    find build/libs -name "*.jar" ! -name "*-plain.jar" \
    -exec cp {} /app/app.jar \;

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
