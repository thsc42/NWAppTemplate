package skeletons;

import networks.ConnectionCreatedListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppLogicPrimeNumberExchange implements ConnectionCreatedListener {
    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) {
        System.out.println("new connection created");

        // proof that we are intelligent beings. Send first five prime numbers
        try {
            os.write(2);
            os.write(3);
            os.write(5);
            os.write(7);
            os.write(11);

            int i = -1;
            do {
                System.out.println("going to read");
                i = is.read();
                System.out.println("read: " + i);
                if (i == -1) {
                    System.out.println("no more data in stream");
                }
                if (i != 2 && i != 3 && i != 5 && i != 7 && i != 11) {
                    System.out.println("there is no intelligente life on the other side :(");
                } else {
                    System.out.println("prime number, is there intelligent live on the other side?");
                }
            }while (i > 0);
        } catch (IOException e) {
            System.err.println("give up: " + e.getLocalizedMessage());
        }

    }
}
