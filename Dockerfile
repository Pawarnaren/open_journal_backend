FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/open_journal_backend-0.0.1-SNAPSHOT.jar"]