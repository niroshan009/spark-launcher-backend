# What is this project
This is a project contains an API to submit a spark job utilizing spark-launcher provided by [apache-spark](https://spark.apache.org/docs/latest/api/java/org/apache/spark/launcher/SparkLauncher.html).

# How to use?
This is a spring-boot project. This project can be imported to your favorite IDE and run.

# API Endpoint
Following endpoint is exposed to trigger a spark job

## POST
This project has an endpoint called ```localhost:8080/spark/start```
```json
{
  "sparkHome": "string",
  "appResource": "string",
  "mainClass": "string",
  "master": "string",
  "args": [
    "string"
  ]
}

```
| Key           | Value                                                                                                     |
|---------------|-----------------------------------------------------------------------------------------------------------|
| `sparkHome`   | Location of your spark home. Can be downloaded from [here](https://spark.apache.org/downloads.html)       |
| `appResource` | Location of your artifact you want to run. Typcally jar file location which contains the spark logic      |
| `mainClass`   | Class you want to execute of your above jar artifact                                                      |
| `master`      | Where you want to run your application. If cluster, you may provide cluster URL                           |
| `args`        | Additional arguments for your application. Application will access these parameters as args[0]....args[x] |



### example 
```json
{
  "sparkHome": "/Users/kd/Development_tools/spark-3.4.0-bin-hadoop3",
  "appResource": "/Users/kd/git/spark/spark-test/spark-job/target/spark-job-1.0-SNAPSHOT.jar",
  "mainClass": "com.kd.main.ReadToDataset",
  "master": "local[*]",
  "args": [
    "/Users/kd/git/spark/spark-test/spark-job/inventory.csv"
  ]
}
```

This is a just a concept to how to trigger a spark job from an endpoint. You can tweak the above application to utilise the `application.properties` file to configure your jar file location, spark home  