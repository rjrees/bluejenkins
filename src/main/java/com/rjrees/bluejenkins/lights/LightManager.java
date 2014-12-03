package com.rjrees.bluejenkins.lights;

import com.rjrees.bluejenkins.jenkins.BuildStatus;
import jssc.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by richard on 30/11/14.
 */

@Component
public class LightManager {
    private static Logger LOGGER = LoggerFactory.getLogger(LightManager.class);

    @Value("${comm.port}")
    private String commPort;

    @Value("${comm.speed}")
    private int commSpeed;

    public LightManager() {
    }

    public void update(BuildStatus status1, BuildStatus status2) {
        LOGGER.info("Updating lights on " + commPort);
        LOGGER.info(status1.toString());
        LOGGER.info(status2.toString());

        try {
            SerialPort serialPort = new SerialPort(commPort);
            serialPort.openPort();
            serialPort.setParams(commSpeed, 8, 1, 0);

            // Send the command here :)

            serialPort.closePort();
        } catch (Exception ex) {
            LOGGER.error("Failed to set lights", ex);
        }


    }
}
