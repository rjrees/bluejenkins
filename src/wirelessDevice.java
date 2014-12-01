import javax.bluetooth.*;


public class wirelessDevice implements DiscoveryListener {
    LocalDevice localDevice = null;

    public wirelessDevice (){
        //setting the port number using Atinav's BCC
        BCC.setPortName("COM1");

        //setting the baud rate using Atinav's BCC
        BCC.setBaudRate(57600);

        //connectable mode using Atinav's BCC
        BCC.setConnectable(true);

        //Set discoverable mode using Atinav's BCC
        BCC.setDiscoverable(DiscoveryAgent.GIAC);

        try{
            localDevice = LocalDevice.getLoaclDevice();
            localDevice.getBluetoothAddress()
        }
        catch (BluetoothStateException exp) {
        }

        // implementation of methods in DiscoveryListener class
        // of javax.bluetooth goes here

        // now do some work
    }
}
