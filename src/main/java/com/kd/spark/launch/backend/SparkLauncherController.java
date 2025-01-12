package com.kd.spark.launch.backend;

import com.kd.spark.launch.backend.model.SparkConfigDTO;
import com.kd.spark.launch.backend.model.SparkJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping(value = "/spark")
public class SparkLauncherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SparkLauncherController.class);

    Map<String, SparkAppHandle> sparkLauncherMap = new HashMap<>();



    @PostMapping("/start")
    public ResponseEntity createJournalEntry(@RequestBody SparkConfigDTO sparkConfigDTO) throws IOException {
        LOGGER.info("Submitting spark job");
        String[] appArgs = sparkConfigDTO.getArgs().toArray(new String[0]);
        Map<String, String> config = new HashMap<>();

        log.info("Running application for configuration: {}", sparkConfigDTO.toString());

        SparkLauncher launcher = new SparkLauncher()
//                .setSparkHome(sparkConfigDTO.getSparkHome())
                .setAppResource(sparkConfigDTO.getAppResource())
                .setAppName("demo-spark-job")
                .setDeployMode("cluster")
                .setMainClass(sparkConfigDTO.getMainClass())
                .setMaster(sparkConfigDTO.getMaster())  // Kubernetes API endpoint
                .setConf("spark.kubernetes.authenticate.driver.serviceAccountName", "spark")
                .setConf("spark.kubernetes.authenticate.executor.serviceAccountName", "spark")
                .setConf("spark.kubernetes.container.image", sparkConfigDTO.getDockerImage())
                .setConf("spark.kubernetes.namespace", "default")
                .setConf("spark.executor.instances", "1")
                .setConf("spark.eventLog.enabled","true")
                .setConf("spark.eventLog.dir", sparkConfigDTO.getEventLogDir())
                // s3 resources for event logs
                .setConf("spark.hadoop.fs.s3a.access.key", sparkConfigDTO.getS3AccessKey())
                .setConf("spark.hadoop.fs.s3a.secret.key", sparkConfigDTO.getS3AccessSecret())
                .setConf("spark.hadoop.fs.s3a.endpoint", sparkConfigDTO.getS3Endpoint())
                .setConf("spark.hadoop.fs.s3a.connection.ssl.enabled", sparkConfigDTO.getSslEnabled())
                .setConf("spark.kubernetes.file.upload.path", sparkConfigDTO.getKubeFileUploadPath())
                .setConf("spark.kubernetes.driver.label.spark-role", "driver")

                .setConf("spark.driver.memory", "1g")
                .setConf("spark.executor.memory", "1g")
                .setConf("spark.executor.cores", "1")
                .setConf("spark.executor.instances", "1")
                .setConf("spark.driver.host", sparkConfigDTO.getDriverHost())
                .setConf("spark.driver.port", sparkConfigDTO.getDriverPort())
                .setConf("spark.driver.extraJavaOptions", "--class "+sparkConfigDTO.getMainClass())

                // spark driver configuration
//                .setConf("spark.driver.bindAddress", sparkConfigDTO.getDriverBindAddress()) // Bind to all interfaces
//                .setConf("spark.driver.port", sparkConfigDTO.getDriverPort())
//                .setConf("spark.driver.host", sparkConfigDTO.getDriverHost()) // Use this if you're on Docker Desktop
                .addAppArgs(appArgs);

//        launcher.addJar("/opt/spark/jars/aws-java-sdk-bundle-1.12.271.jar");
//        launcher.addJar("/opt/spark/jars/hadoop-aws-3.3.4.jar");

        launcher.setVerbose(true);
        launcher.startApplication(new SparkAppHandle.Listener() {

            @Override
            public void stateChanged(SparkAppHandle sparkAppHandle) {

                log.info("state changed to : {}, in spark job: {}", sparkAppHandle.getState(), sparkAppHandle.getAppId());

                if(sparkLauncherMap.containsKey(sparkAppHandle.getAppId())){
                    sparkLauncherMap.remove(sparkAppHandle.getAppId());
                }

                sparkLauncherMap.put(sparkAppHandle.getAppId(), sparkAppHandle);
            }

            @Override
            public void infoChanged(SparkAppHandle sparkAppHandle) {
                log.info("Info changed to {}", sparkAppHandle.getState());

            }
        });
        launcher.redirectToLog(LOGGER.getName());

        return ResponseEntity.ok("started the job");
    }

    @PostMapping("/stop")
    public ResponseEntity stopSparkJob(@RequestBody SparkJob sparkJob) throws IOException {
        LOGGER.info("Stopping spark job with appId: {}", sparkJob.getJobId());

        sparkLauncherMap.keySet().stream().forEach(e -> {
            log.info("existing app ids: {}", e);
        });


        SparkAppHandle handle = sparkLauncherMap.get(sparkJob.getJobId());
        if(handle != null){
            handle.stop();
            return  ResponseEntity.ok("Job stopped");
        }
        return  ResponseEntity.ok("Job stopped");
    }

    @GetMapping("/status")
    public ResponseEntity getStatus(){
        LOGGER.debug("Checking the status");
        return ResponseEntity.ok("Hello world");
    }
}
