package com.kd.spark.launch.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkConfigDTO {

    private String sparkHome;

    private String appResource;

    private String mainClass;

    private String master;

    private List<String> args;

}
