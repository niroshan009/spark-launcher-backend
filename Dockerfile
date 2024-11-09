FROM spark:3.5.0-java17
LABEL authors="kd"
VOLUME /tmp
ENV SPARK_HOME=/opt/spark
WORKDIR /app

COPY ./target/*.jar /app/myapp.jar

RUN wget -q https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.3.4/hadoop-aws-3.3.4.jar \
    && wget -q https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.12.271/aws-java-sdk-bundle-1.12.271.jar \
    && mv hadoop-aws-3.3.4.jar $SPARK_HOME/jars/ \
    && mv aws-java-sdk-bundle-1.12.271.jar $SPARK_HOME/jars/

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/myapp.jar"]