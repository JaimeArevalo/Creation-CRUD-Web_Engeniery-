# Forzar build limpio en Railway
# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Configurar Maven para usar mirror más confiable y aumentar timeouts
COPY settings.xml /root/.m2/settings.xml

# Copiar solo el pom.xml primero para aprovechar la caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests -Dmaven.wagon.http.readTimeout=180000

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Instalar curl para healthchecks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/app.jar app.jar
EXPOSE 8080

# Configurar variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"] 