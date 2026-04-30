# Étape de build
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et compiler
COPY src ./src
RUN mvn clean package -DskipTests

# Étape d'exécution
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copier le .jar généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port de l'application (8081)
EXPOSE 8081

# Démarrer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
