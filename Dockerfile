FROM openjdk:19
ARG JAR_FILE
COPY target/appointmentscheduler-[0-9].[0-9].[0-9].jar application.jar
ENTRYPOINT ["java","-XX:+UseContainerSupport", "-jar", "application.jar"]
