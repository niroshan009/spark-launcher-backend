package com.kd.spark.launch.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SparkConfigDTO {

    private String sparkHome;

    private String appResource;

    private String mainClass;

    private String master;

    private String eventLogDir;

    private String s3AccessKey;

    private String s3AccessSecret;

    private String s3Endpoint;

    private String driverBindAddress;

    private String driverPort;

    private String driverHost;

    private String dockerImage;
    private List<String> args;

}
