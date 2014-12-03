package com.rjrees.bluejenkins.lights;

import com.rjrees.bluejenkins.jenkins.BuildStatus;
import jssc.SerialPort;
import jssc.SerialPortException;
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
            StringBuilder command = new StringBuilder();
            command.append("SEQ:");
            command.append(mapStatus(status1));
            command.append(" ");
            command.append(mapStatus(status2));
            command.append("\n");

            serialPort.writeString(command.toString());

            serialPort.closePort();
        } catch (SerialPortException ex) {
            LOGGER.error("Serial problem ", ex);
        } catch (Exception ex) {
            LOGGER.error("Failed to set lights", ex);
        }
    }

    private String mapStatus(BuildStatus status) {
        StringBuilder pattern = new StringBuilder();

        try {
            if (status.isBuilding()) {
                pattern.append("f");
            }

            switch( status.getLastCompletedResult()) {
                case "UNSTABLE" :
                    pattern.append("hazards");
                    break;
                case "SUCCESSFULL" :
                    pattern.append("reverse");
                    break;
                case "FAILURE":
                    pattern.append("breaks");
                    break;
            }

            return pattern.toString();
        } catch (Exception ex) {
            LOGGER.error("Error mapping status" , ex);
            return "CYLON";
        }
    }
}
