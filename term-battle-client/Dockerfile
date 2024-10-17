FROM maven:3.9.9-jdk-21

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install
CMD ["mvn", "exec:java", "-Dexec.mainClass=com.pkg.app.Main"]
