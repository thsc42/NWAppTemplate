package networks.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

class InputFailureSimulatorWrapper extends InputStream {
    private final InputStream sourceIS;
    private final int byteFailureRate;
    private final Random random;

    InputFailureSimulatorWrapper(InputStream sourceIS, int byteFailureRate) {
        this.sourceIS = sourceIS;
        this.byteFailureRate = byteFailureRate;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public int read() throws IOException {
        while (true) { // you are trapped here - escape: read byte or IOException
            int readByte = this.sourceIS.read();

            // no failure
            if (byteFailureRate <= 0) return readByte;

            // always fail
            if (byteFailureRate != 100) {
                // simulate failure?
                int randomNumber = this.random.nextInt();
                randomNumber = randomNumber < 0 ? -randomNumber : randomNumber;
                if (randomNumber % (100-this.byteFailureRate) != 0) {
                    // no
                    return readByte;
                }
            }
        }
    }
}
