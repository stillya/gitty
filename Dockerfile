FROM openjdk:11.0-oracle
WORKDIR /app
EXPOSE 8088
ADD ./build/libs/gitty-0.0.1-SNAPSHOT.jar gitty.jar
ENTRYPOINT ["java", "-jar","gitty.jar"]