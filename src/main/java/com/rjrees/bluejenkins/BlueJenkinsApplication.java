package com.rjrees.bluejenkins;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by richard on 30/11/14.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class BlueJenkinsApplication {
    private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BlueJenkinsApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Starting BlueJenkins");
        ApplicationContext ctx = SpringApplication.run(BlueJenkinsApplication.class, args);
    }
}
