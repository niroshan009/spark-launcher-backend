package com.kd.spark.launch.backend;

import com.kd.spark.launch.backend.model.SparkConfigDTO;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(value = "/spark")
public class SparkLauncherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkLauncherController.class);

    @PostMapping("/start")
    public ResponseEntity createJournalEntry(@RequestBody SparkConfigDTO sparkConfigDTO) throws IOException {

        String[] appArgs = sparkConfigDTO.getArgs().toArray(new String[0]);

        SparkLauncher launcher = new SparkLauncher()
                .setSparkHome(sparkConfigDTO.getSparkHome())
                .setAppResource(sparkConfigDTO.getAppResource())
                .setMainClass(sparkConfigDTO.getMainClass())
                .setMaster(sparkConfigDTO.getMaster())
                .addAppArgs(appArgs);

        launcher.startApplication();
        launcher.redirectToLog(LOGGER.getName());
        return ResponseEntity.ok("started the job");
    }
}
