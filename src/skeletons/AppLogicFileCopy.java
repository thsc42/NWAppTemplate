package skeletons;

import networks.ConnectionCreatedListener;

import java.io.*;

public class AppLogicFileCopy implements ConnectionCreatedListener {
    private final String fileName;

    public AppLogicFileCopy(String fileName) {
        this.fileName = fileName;
    }

    void copy(OutputStream os, InputStream is) throws IOException {
        int value = 0;
        while(value > -1) {
            value = is.read();
            if(value > -1) os.write(value);
            else os.close();
        }
    }

    @Override
    public void connectionCreated(InputStream is, OutputStream os, boolean asServer, String otherPeerAddress) {
        InputStream sourceIS = null;
        OutputStream targetOS = null;

        try {
            if(asServer) {
                // I am data sink
                // open file output stream
                targetOS = new FileOutputStream(this.fileName);
                // what is source IS?
                sourceIS = is;
            } else {
                // I am data source
                // open file input stream and write into output stream
                sourceIS = new FileInputStream(this.fileName);
                // what is targetOS?
                targetOS = os;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            System.err.println("give up");
            return;
        }

        try {
            this.copy(targetOS, sourceIS);
            sourceIS.close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
