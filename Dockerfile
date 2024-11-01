FROM openjdk:21-jdk-slim
ENV JAVA_HOME=/usr/local/openjdk-17
WORKDIR /app
COPY target/Library-Management-System-0.0.1-SNAPSHOT.jar /app/library.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/library.jar", "mvn spring-boot:run"]
