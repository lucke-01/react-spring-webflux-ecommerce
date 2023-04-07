FROM maven:3.8.6-amazoncorretto-17 AS build
RUN mkdir /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests


FROM openjdk:17
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar" ,"/usr/local/lib/app.jar"]