package skeletons;

import networks.ConnectionCreatedListener;
import networks.utils.StreamConnectionFactory;

public class FileCopyEndpoint {
    public static final int PORTNUMBER = 7777;

    public static void main(String[] args) throws Exception {
        ConnectionCreatedListener appLogicFileCopy = null;
        if(args.length < 1) {
            System.err.println("missing arguments - at least file name required");
            System.err.println("variant 1: one parameter: [file] - stores received data into a file");
            System.err.println("variant 2: two parameters: [file host] - copy data from file to another process on host");
        } else if(args.length == 1) {
            System.out.println("run as server on port " + PORTNUMBER + " store file under name " + args[0]);
            appLogicFileCopy = new AppLogicFileCopy(args[0]);
        } else if(args.length == 2) {
            System.out.println("try to connect to host " + args[0] + " to copy file " + args[1]);
            appLogicFileCopy = new AppLogicFileCopy(args[1]);
        } else {
            System.err.println("too much parameters");
        }

        if(appLogicFileCopy != null) {
            System.out.println("launch connection factory");
            StreamConnectionFactory streamConnectionFactory = new StreamConnectionFactory(PORTNUMBER);
            streamConnectionFactory.addConnectionListener(appLogicFileCopy);

            if(args.length > 1) streamConnectionFactory.open(false);
            else streamConnectionFactory.connect(args[0], 1, 20);
        }
    }
}
