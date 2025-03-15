# Основной образ с Maven и OpenJDK 17 для сборки проекта
FROM maven:3.9.9-amazoncorretto-17 AS build

# Указываем рабочую директорию
WORKDIR /app

# Копируем pom.xml и загружаем зависимости
COPY pom.xml .

# Копируем все файлы проекта
COPY . /app

# Сборка проекта с помощью Maven, пропуская тесты
RUN mvn clean install -DskipTests

# Создаем финальный образ на базе OpenJDK 17
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем скомпилированный .jar файл из предыдущего этапа сборки
COPY --from=build /app/notification-ms/target/*.jar app.jar

# Открываем порт 8790
EXPOSE 8790

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]