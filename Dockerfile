FROM openjdk:19
ARG JAR_FILE
COPY build/libs/com.vit_ana.scheduler-[0-9].[0-9].[0-9].jar application.jar
ENTRYPOINT ["java","-XX:+UseContainerSupport", "-jar", "application.jar"]
