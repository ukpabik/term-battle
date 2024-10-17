FROM maven:3.9.9-jdk-21

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY .env .env 

RUN mvn clean install
EXPOSE 8080

CMD ["mvn", "exec:java", "-Dexec.mainClass=com.pkg.app.Main"]
