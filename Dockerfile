FROM adoptopenjdk/openjdk11:jre-11.0.15_10-alpine

EXPOSE 8081

ADD target/cloud_back-0.0.1-SNAPSHOT.jar back.jar

ENTRYPOINT ["java", "-jar", "/back.jar" ]
