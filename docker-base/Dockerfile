FROM eclipse-temurin:21-jdk

RUN apt-get update && apt-get install -y wget tar
RUN wget https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz \
    && tar -zxvf apache-maven-3.9.9-bin.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.9.9 /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/bin/mvn \
    && rm apache-maven-3.9.9-bin.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV MAVEN_CONFIG="/root/.m2"

RUN java -version && mvn -version
RUN apt-get clean && rm -rf /var/lib/apt/lists/*

