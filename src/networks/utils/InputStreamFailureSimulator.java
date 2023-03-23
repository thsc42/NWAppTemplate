package networks.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

class InputStreamFailureSimulator extends InputStream {
    private final int bitFailureRate;
    private final boolean gauss;
    private final InputStream is;
    private final Random random;

    public InputStreamFailureSimulator(int bitFailureRate, boolean gauss, InputStream is) {
        this.bitFailureRate = bitFailureRate;
        this.gauss = gauss;
        this.is = is;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public int read() throws IOException {
        // read from real input stream
        int readInt = this.is.read();

        // note: we work only on byte. We have to deal only with least significant 8 bits.
        int mask2Change = 0x1; // one bit at position 0
        int mask2Keep = 0xFE; // all bits set except position 0
        for(int bitPosition = 0; bitPosition < 8; bitPosition++) {
            if(this.produceBitFailure()) {
                // switch bit
                int unchangedBits = readInt & mask2Keep;
                int bit2Change = readInt & mask2Change;
                if(bit2Change == 0) {
                    // bit was not set - set it no
                    int bit2Set = 0x1;
                    bit2Set <<= bitPosition;
                    readInt = bit2Set | unchangedBits;
                } else { // nothing to do. Was set - is already unset.
                    readInt = unchangedBits;
                }
            }
            // shift masks
            mask2Change <<= 1;
            mask2Keep <<=1;
            mask2Keep |= 1;
        }

        return readInt;
    }

    private boolean produceBitFailure() {
        int randomNumber = random.nextInt();
        randomNumber = randomNumber < 0 ? -randomNumber : randomNumber;
        randomNumber %= 100;

        // now we have a value between 0 and 99
        return randomNumber < this.bitFailureRate;
    }
}
