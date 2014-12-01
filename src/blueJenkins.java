/**
 * Created by richard on 30/11/14.
 */
public class blueJenkins {

    public static void main(String[] args) {
        try {
            ( new Messenger() ).connect( "/dev/ttyUSB0" );
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }



}
