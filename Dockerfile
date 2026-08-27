FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

RUN find target -maxdepth 1 -type f -name "*.war" ! -name "original-*.war" -exec cp {} /app/app.war \;

EXPOSE 8080

CMD ["java", "-jar", "/app/app.war"]