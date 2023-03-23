package networks.layer2;

import networks.ConnectionCreatedListener;

import java.io.*;

public class HandleBitFailure implements ConnectionCreatedListener {
    private static final int MAX_SIGNS_IN_LINE = 10;
    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) {
        boolean send = asServer;
        int signInLine = MAX_SIGNS_IN_LINE;
        boolean isSender = asServer; // arbitrary choice
        if (isSender) System.out.println("send number 0 to 100");
        else System.out.println("expect numbers 0 to 100: O .. okay | x .. failure on channel");

            try {
            for(int round = 0; round < 100; round++) {
                if (isSender) os.write(round); // write
                else { // read data
                    int readInt = is.read();
                    if (readInt == round) System.out.print("O"); // no failure
                    else System.out.print("X"); // failure

                    // make output better readable
                    if (--signInLine == 0) {
                        System.out.print('\n');
                        signInLine = MAX_SIGNS_IN_LINE;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("problems: " + e.getLocalizedMessage());
        }
    }
}