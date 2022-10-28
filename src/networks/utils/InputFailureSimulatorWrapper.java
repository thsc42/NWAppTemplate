package networks.utils;

import net.sharksystem.utils.AlarmClock;
import net.sharksystem.utils.AlarmClockListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

class InputFailureSimulatorWrapper extends InputStream implements AlarmClockListener {
    public static final int MAX_READ_WAIT_MILLIS = Integer.MAX_VALUE;
//    public static final int MAX_READ_WAIT_MILLIS = 500;
    private final InputStream sourceIS;
    private final int byteFailureRate;
    private final Random random;

    InputFailureSimulatorWrapper(InputStream sourceIS, int byteFailureRate) {
        this.sourceIS = sourceIS;
        this.byteFailureRate = byteFailureRate;
        this.random = new Random(System.currentTimeMillis());
    }

    private class ByteReaderThread extends Thread {
        private final InputStream is;
        int readInt;
        boolean successfullyRead = false;
        IOException ioException = null;

        ByteReaderThread(InputStream is) throws IOException {
            this.is = is;
        }

        public void run() {
            try {
                this.readInt = is.read();
            } catch (IOException e) {
                this.ioException = e;
            }
        }
    }

    private Thread threadWaitingForReadingToEnd = null;

    @Override
    public void alarmClockRinging(int i) {
        if(this.threadWaitingForReadingToEnd != null) {
            this.threadWaitingForReadingToEnd.interrupt();
            this.threadWaitingForReadingToEnd = null;
        }
    }

    @Override
    public int read() throws IOException {
        while (true) { // you are trapped here - escape: read byte or IOException
            // start read thread
            ByteReaderThread readThread = new ByteReaderThread(this.sourceIS);

            // set an alarm before reading
            this.threadWaitingForReadingToEnd = Thread.currentThread();
            AlarmClock alarmClock = new AlarmClock(MAX_READ_WAIT_MILLIS, this);
            alarmClock.start();

            // start..
            readThread.start();
            // .. and wait
            try {
                readThread.join();
                alarmClock.kill();
            } catch (InterruptedException e) {
                // alarm kicked in
                throw new IOException("stopped reading - max idle time exceeded");
            }

            // problems while reading
            if(readThread.ioException != null) throw readThread.ioException;

            // no problems - get read byte
            int readByte = readThread.readInt;
            if(readByte == -1) return -1; // eof

            // no failure
            if (byteFailureRate <= 0) return readByte;

            // always fail
            if (byteFailureRate != 100) {
                // simulate failure?
                int randomNumber = this.random.nextInt();
                randomNumber = randomNumber < 0 ? -randomNumber : randomNumber;
                randomNumber %= 100;
                // 99 nearly always... 1 hardly

                if (randomNumber > this.byteFailureRate) {
                    // no
                    return readByte;
                }
            }
        }
    }

}
