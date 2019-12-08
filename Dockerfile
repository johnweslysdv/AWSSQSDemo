FROM openjdk:10-jre-slim
COPY ./target/AWSSqsDemo-0.0.1-SNAPSHOT.jar ./
WORKDIR ./
EXPOSE 9090
CMD ["java", "-jar", "AWSSqsDemo-0.0.1-SNAPSHOT.jar"]
