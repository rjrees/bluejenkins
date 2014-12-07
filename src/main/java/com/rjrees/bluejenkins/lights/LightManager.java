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

   private SerialPort serialPort;

   public LightManager() {
   }


   public void update(BuildStatus status1, BuildStatus status2) {
      LOGGER.info("Updating lights on " + commPort);
      LOGGER.info(status1.toString());
      LOGGER.info(status2.toString());


      // Send the command here :)
      StringBuilder command = new StringBuilder();
      command.append("SEQ:");
      command.append(mapStatus(status1));
      command.append(" ");
      command.append(mapStatus(status2));
      command.append("\n");

      try {

         writeCommandToSerial( command.toString() );

      } catch (SerialPortException ex) {
         LOGGER.error("Serial problem ", ex);
      } catch (Exception ex) {
         LOGGER.error("Failed to set lights", ex);
      }

   }

   /**
    * Write the specified commmand to the serial port
    * Check first if the port is open,, if not open the port
    * @param command
    * @throws SerialPortException
    */
   private void writeCommandToSerial(final String command) throws SerialPortException {
      if( null == serialPort || !serialPort.isOpened()) {
         openSerial();
      }

      if( !serialPort.isOpened()) {
           openSerial();
      }

      LOGGER.info(command.toString());
      serialPort.writeString(command.toString());
   }

   private void openSerial() throws SerialPortException {
      LOGGER.info("Opening Serial Port");
      if( null != serialPort) {
         serialPort.closePort();
         serialPort = null;
      }
      serialPort = new SerialPort(commPort);
      serialPort.openPort();
      serialPort.setParams(commSpeed, 8, 1, 0);
   }


   /**
    * @throws SerialPortException
    */
   private void closeSerial() throws SerialPortException {
      serialPort.closePort();
   }


   private String mapStatus(BuildStatus status) {
      StringBuilder pattern = new StringBuilder();

      try {
         if (status.isBuilding()) {
            pattern.append("f");
         }

         switch (status.getLastCompletedResult()) {
            case "UNSTABLE":
               pattern.append("hazards");
               break;
            case "SUCCESS":
               pattern.append("reverse");
               break;
            case "FAILURE":
               pattern.append("breaks");
               break;
            default:
               pattern.append("CYLON");
         }

         return pattern.toString();
      } catch (Exception ex) {
         LOGGER.error("Error mapping status", ex);
         return "CYLON\n";
      }
   }
}
