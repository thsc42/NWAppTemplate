package skeletons;

import networks.ConnectionCreatedListener;

import java.io.*;

public class AppLogicChat implements ConnectionCreatedListener {
    public static final String STOP_WORD = "bye";
    public static final String ASK_FOR_INPUT = "please type something ('bye' to end conversion)";
    public static final String PROMPT = "read: ";
    public static final String WAIT_FOR_MESSAGE = "wait for message";

    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) {

        // prepare object to read from console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // prepare object to read/write utf string over the wire
        DataOutputStream daos = new DataOutputStream(os);
        DataInputStream dais = new DataInputStream(is);

        // proof that we are intelligent beings. Send first five prime numbers
        try {
            boolean again = asServer;
            do {
                if(again) {
                    // read reply
                    System.out.println(PROMPT + WAIT_FOR_MESSAGE);
                    String lineFromOtherUser = dais.readUTF();
                    System.out.println(PROMPT + lineFromOtherUser);
                }

                System.out.println(ASK_FOR_INPUT);
                String lineFromUser = reader.readLine();

                // type
                again = !lineFromUser.equalsIgnoreCase(STOP_WORD);

                if(again) {
                    // send it
                    daos.writeUTF(lineFromUser);
                }

            } while(again);

        } catch (IOException e) {
            System.err.println("give up: " + e.getLocalizedMessage());
        }

    }
}
