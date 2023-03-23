package skeletons;

import networks.utils.StreamConnectionFactory;

public class Endpoint {
    private static final int PORTNUMBER = 7777;

    public static void main(String[] args) throws Exception {
        StreamConnectionFactory streamConnectionFactory = new StreamConnectionFactory(PORTNUMBER);

//        streamConnectionFactory.addConnectionListener(new AppLogicPrimeNumberExchange());
//        streamConnectionFactory.addConnectionListener(new AppLogicChat());
        streamConnectionFactory.addConnectionListener(new AppLogicDataExchange());

        if(args.length < 1) {
            System.out.println("run as server and accept connection requests on port " + PORTNUMBER);
            streamConnectionFactory.open(false);
        } else {
            System.out.println("try to connect to host " + args[0] + " on port " + PORTNUMBER);
            streamConnectionFactory.connect(args[0], 1, 20);
        }
    }
}
