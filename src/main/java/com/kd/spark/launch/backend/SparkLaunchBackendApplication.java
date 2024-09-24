package com.kd.spark.launch.backend;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@SpringBootApplication
public class SparkLaunchBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkLaunchBackendApplication.class, args);
	}

}
