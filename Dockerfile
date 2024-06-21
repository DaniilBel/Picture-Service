FROM maven:3.8.5-openjdk-17 as build
WORKDIR /
COPY checkstyle-suppressions.xml /
COPY pom.xml .
RUN mvn dependency:go-offline
COPY /src /src
RUN mvn -f /pom.xml clean package

FROM openjdk:17-jdk-alpine3.14
COPY --from=build /target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
