FROM amazoncorretto:17

COPY target/*.jar schedule-api.jar
ENTRYPOINT ["java","-jar","/schedule-api.jar"]