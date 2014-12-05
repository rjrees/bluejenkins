package com.rjrees.bluejenkins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by Gordon on 03/12/2014.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    @Autowired
    private UpdateController theJob;

    /**
     * This is the scheduled task that runs every 30 seconds
     * 1) reads the status of the jobs from Jenkins
     * 2) update the lights status
     */
    @Scheduled(fixedDelay=30000)
    public void doSomething() {
        theJob.updateLights();
    }
}
