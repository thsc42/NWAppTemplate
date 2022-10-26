package networks;

import java.io.*;

/**
 * This class can be used as template to implement decentralized apps. Each peer object is an endpoint
 * of a stream based communication.
 */
public class ExamplePeer implements ConnectionCreatedListener, Runnable {

    private final int port;
    private final String name;
    private final boolean asServer;

    private boolean setupProblem = false;

    public ExamplePeer(String name, int port, boolean asServer) {
        this.name = name;
        this.port = port;
        this.asServer = asServer;
    }

    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        if(asServer) {
            sb.append(" accepted a connection from ");
        } else {
            sb.append(" created a connection to ");
        }
        sb.append(otherPeerAddress);
        System.out.println(sb.toString());

        try {
            Thread.sleep(1); // wait a moment for a better output
            System.out.println("run a dummy protocol | conversation to illustrate usage");
            // do some example stuff
            DataInputStream dais = new DataInputStream(is);
            DataOutputStream daos = new DataOutputStream(os);

            int round = 5;
            boolean talk = asServer;
            while(round > 0) {
                for(int i = 0; i < 2; i++) {
                    // switch roles in each round
                    if(talk) {
                        daos.writeUTF("I am " + this.name);
                        daos.writeInt(round);
                    } else {
                        String readUTFText = dais.readUTF();
                        int readInt = dais.readInt();
                        sb = new StringBuilder();
                        sb.append(this.name);
                        sb.append(". I received: [utfText: ");
                        sb.append(readUTFText);
                        sb.append(", int:  ");
                        sb.append(readInt);
                        sb.append("]");
                        System.out.println(sb.toString());
                    }
                    talk = !talk;
                }
                round--;
            }
            System.out.println("end dummy protocol");
        } catch (InterruptedException | IOException e) {
            System.err.println("something realy bad happened: " + e.getLocalizedMessage());
        }
    }

    public boolean isSetupProblem() {
        return this.setupProblem;
    }

    @Override
    public void run() {
        try {
            // create or offer a connection - our factory will help
            StreamConnectionFactory factory = new StreamConnectionFactory(this.port);
            // this peer listens for a new connection
            factory.addConnectionListener(this);

            if(this.asServer) {
                // open a connection
                    factory.open(false); // just one connection
            } else {
                // try to connect - use localhost, try 3 times, wait a second
                factory.connect("localhost", 1, 3);
            }
        } catch (Exception e) {
            System.err.println("problems during connection establishment: " + e.getLocalizedMessage());
            this.setupProblem = true; // good guess
        }
    }
}
