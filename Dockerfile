FROM eclipse-temurin:21-jdk
WORKDIR /cinema
COPY build/libs/cinema-78593bec.jar app.jar
CMD ["java","-jar","app.jar"]
EXPOSE 8080
