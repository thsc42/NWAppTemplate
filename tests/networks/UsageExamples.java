package networks;

import org.junit.jupiter.api.Test;

public class UsageExamples {
    @Test
    public void usage1() throws InterruptedException {
        ExamplePeer alicePeer = new ExamplePeer("Alice", 3333, true); // asServer
        ExamplePeer bobPeer = new ExamplePeer("Bob", 3333, false); // !asServer

        Thread aliceThread = new Thread(alicePeer);
        Thread bobThread = new Thread(bobPeer);

        aliceThread.start();
        bobThread.start();

        // wait and let Alice and Bob talk
        Thread.sleep(3000);
    }
}
