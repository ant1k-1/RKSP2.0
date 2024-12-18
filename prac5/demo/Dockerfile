# Build stage
FROM gradle:8.7-jdk17 AS builder
WORKDIR /usr/app
COPY . .
# Сборка артефакта
RUN gradle bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre
LABEL authors="Anton Mokin"

# Указываем рабочую директорию
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

# Копируем скомпилированный JAR из стадии сборки
COPY --from=builder /usr/app/build/libs/demo-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт 8080 (или фиксированный порт)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]