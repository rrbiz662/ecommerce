FROM openjdk:11-jdk
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth-course-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","auth-course-0.0.1-SNAPSHOT.jar"]