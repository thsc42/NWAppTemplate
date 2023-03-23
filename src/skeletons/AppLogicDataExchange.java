package skeletons;

import networks.ConnectionCreatedListener;

import java.io.*;

public class AppLogicDataExchange implements ConnectionCreatedListener {
//    public static final long LONG_EXAMPLE = Long.MAX_VALUE;
    public static final long LONG_EXAMPLE = 424242;
    public static final double DOUBLE_EXAMPLE = 3.14159;
    public static final String STRING_EXAMPLE = "ExampleString";

    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) {
        System.out.println("data exchange example");

        DataOutputStream daos = new DataOutputStream(os);
        DataInputStream dais = new DataInputStream(is);

        // proof that we are intelligent beings. Send first five prime numbers
        try {
            boolean reader = asServer;
            if(reader) {
                this.readExampleData(dais);
            } else {
                this.writeExampleData(daos);
            }
        } catch (IOException e) {
            System.err.println("give up: " + e.getLocalizedMessage());
        }
    }

    private void writeExampleData(DataOutputStream daos) throws IOException {
        daos.writeLong(LONG_EXAMPLE);
        System.out.println("wrote long: " + LONG_EXAMPLE);

        daos.writeDouble(DOUBLE_EXAMPLE);
        System.out.println("wrote double: " + DOUBLE_EXAMPLE);

        daos.writeUTF(STRING_EXAMPLE);
        System.out.println("wrote string: " + STRING_EXAMPLE);
    }

    private void readExampleData(DataInputStream dais) throws IOException {
        System.out.println("start reading..");

        long longValueRead = dais.readLong();
        System.out.println("read long: " + longValueRead);

        double doubleValueRead = dais.readDouble();
        System.out.println("read double: " + doubleValueRead);

        String stringValueRead = dais.readUTF();
        System.out.println("read string: " + stringValueRead);
    }
}