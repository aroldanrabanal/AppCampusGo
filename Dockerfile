# Fase 1: Compilar la aplicación (Build)
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Ejecutar la aplicación (Runtime)
FROM amazoncorretto:21-al2023-headless
WORKDIR /app
# Copiamos el archivo .jar generado en la fase anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto (aunque Render usa su propia variable $PORT)
EXPOSE 8080

# Comando para ejecutar la app usando el puerto dinámico de Render
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]