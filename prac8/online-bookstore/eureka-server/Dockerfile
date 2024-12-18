# Используем официальный базовый образ с OpenJDK 17 (Temurin)
FROM eclipse-temurin:17-jdk-alpine

# Устанавливаем директорию для приложения
WORKDIR /app

# Аргумент для jar-файла (Maven будет собирать в target/)
ARG JAR_FILE=target/*.jar

# Копируем скомпилированный jar в образ
COPY ${JAR_FILE} app.jar

# Опционально: если нужно, чтобы приложение писало временные файлы в /tmp
VOLUME /tmp

# Экспортируем порт, на котором сервис будет доступен (обычно 8080)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java","-jar","/app/app.jar"]
