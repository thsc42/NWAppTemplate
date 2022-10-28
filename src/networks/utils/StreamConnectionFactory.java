package networks.utils;

import net.sharksystem.utils.Log;
import net.sharksystem.utils.tcp.SocketFactory;
import networks.ConnectionCreatedListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides ways to set up a stream connection. The actual protocol remains opaque to developers.
 * (Spoiler: It will be TCP (in most cases).
 *
 * This class provide means to establish unreliable connection - connections can break down / bit errors can
 * happen.
 */
public class StreamConnectionFactory {
    private final int port;
    private final int byteFailureRate;
    private ServerSocket serverSocket;
    private List<ConnectionCreatedListener> connectionCreatedListeners = new ArrayList<>();
    private ConnectionAttemptsThread connectionAttemptThread = null;

    /**
     * Produce a tcp factory object.
     * @param port port number - either local port or remote port to connect to.
     * @param byteFailureRate This implementation simulates a non-perfect channel. Data can get lost.
     *     Complete bytes can get lost (it is easier to implement than dealing with bits.
*          Principles are the same, though.). It is a probability: 100 == 100% (any package gets lost),
     *                             0 == 0% (no package lost)
     */

    public StreamConnectionFactory(int port, int byteFailureRate) throws Exception {
        this.port = port;

        if(byteFailureRate < 0) throw new Exception("failure rate cannot be a negative number");
        this.byteFailureRate = byteFailureRate;
    }
    public StreamConnectionFactory(int port) throws Exception {
        this(port, 0);
    }

    public void addConnectionListener(ConnectionCreatedListener connectionCreatedListener) {
        this.connectionCreatedListeners.add(connectionCreatedListener);
    }

    private ServerSocket getServerSocket() throws IOException {
        if(this.serverSocket != null) {
            Log.writeLog(this, "server socket already created - do nothing, use it");
        } else {
            this.serverSocket = new ServerSocket(this.port);
            Log.writeLog(this, "server socket created");
        }

        return this.serverSocket;
    }

    /**
     * close port - other peers can no longer connect.
     * @exception IOException cannot close existing server socket - weired.
     */
    public void stopOpen() throws IOException {
        if(this.serverSocket != null) this.serverSocket.close();
    }

    /**
     * Provide an open port other peers can connect to.
     * @param multipleConnections true - port remains open after peer connected / false - port is closed
     * @throws IOException cannot create server port - other potential problems are handled
     */
    public void open(boolean multipleConnections) throws IOException {
        SocketFactory socketFactory = new SocketFactory(this.getServerSocket());

        // run it
        Thread socketFactoryThread = new Thread(socketFactory);
        this.checkClientAndServer();

        socketFactoryThread.start();
        try {
            socketFactoryThread.join();
        } catch (InterruptedException e) {
            // no way this happens
        }

        // tell listeners
        this.notifyConnectionCreatedListeners(socketFactory.getInputStream(),
                socketFactory.getOutputStream(), true, socketFactory.getRemoteAddress());

    }

    /**
     * Create a connection to an existing communication endpoint. This endpoint might not yet be
     * established. This method allows to wait a few seconds to start another attempt.
     *
     * @param waitInSeconds waiting time between connection attempts (in seconds - anything below would
     *                      produce a horrible networks load.
     * @param numberAttempts how many attempts - 0 just one.
     */
    public void connect(String hostName, int waitInSeconds, int numberAttempts) {
        this.connectionAttemptThread = new ConnectionAttemptsThread(hostName, waitInSeconds, numberAttempts);
        this.checkClientAndServer();

        this.connectionAttemptThread.start();
    }

    private class ConnectionAttemptsThread extends Thread {
        private final String hostName;
        private final int waitInSeconds;
        private int numberAttempts;

        public ConnectionAttemptsThread(String hostName, int waitInSeconds, int numberAttempts) {
            this.hostName = hostName;
            this.waitInSeconds = waitInSeconds;
            this.numberAttempts = numberAttempts;
        }

        public void run() {
            StringBuilder sb = new StringBuilder();
            sb.append(" to ");
            sb.append(this.hostName);
            sb.append(" on port ");
            sb.append(StreamConnectionFactory.this.port);
            String partLogText = sb.toString();

            do {
                try {
                    Log.writeLog(this, "try to connect" + partLogText);
                    Socket socket = new Socket(this.hostName, StreamConnectionFactory.this.port);
                    Log.writeLog(this, "connect established" + partLogText);
                    StreamConnectionFactory.this.notifyConnectionCreatedListeners(
                            socket.getInputStream(), socket.getOutputStream(), false,
                            this.hostName + ":" + socket.getPort()
                    );
                    break; // success - we are done here
                } catch (IOException e) {
                    Log.writeLog(this, "connection creation failed" + partLogText);
                }
                this.numberAttempts--;
            } while(this.numberAttempts > 0);
            StreamConnectionFactory.this.connectionAttemptThread = null; // I am leaving now
        }
    }

    private void notifyConnectionCreatedListeners(InputStream is, OutputStream os,
              boolean asServer, String otherPeerAddress) {

        if(this.byteFailureRate > 0) {
            // wrap it
            is = new InputFailureSimulatorWrapper(is, this.byteFailureRate);
        }

        for(ConnectionCreatedListener listener : this.connectionCreatedListeners) {
            listener.connectionCreated(is, os, asServer, otherPeerAddress);
        }
    }

    private void checkClientAndServer() {
        if(this.serverSocket != null && this.connectionAttemptThread != null) {
            Log.writeLog(this, "This peer is open for connections and is about creating connections." +
                    "Multiple connections to another peer could be established - be careful what you are doing.");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //                          wrapper to simulate network failure                          //
    ///////////////////////////////////////////////////////////////////////////////////////////

}
