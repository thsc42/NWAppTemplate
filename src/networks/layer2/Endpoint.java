package networks.layer2;

import networks.utils.StreamConnectionFactory;
import skeletons.AppLogicDataExchange;

public class Endpoint {
    private static final int PORTNUMBER = 7777;

    public static void main(String[] args) throws Exception {
        StreamConnectionFactory streamConnectionFactory = new StreamConnectionFactory(PORTNUMBER, 20, true);
//        StreamConnectionFactory streamConnectionFactory = new StreamConnectionFactory(PORTNUMBER);

        streamConnectionFactory.addConnectionListener(new HandleBitFailure());

        if(args.length < 1) {
            System.out.println("run as server and accept connection requests on port " + PORTNUMBER);
            streamConnectionFactory.open(false);
        } else {
            System.out.println("try to connect to host " + args[0] + " on port " + PORTNUMBER);
            streamConnectionFactory.connect(args[0], 1, 20);
        }
    }
}
