package networks.utils;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class FailureSimulatorTests {
    public static final int NUMBER_DATA = 10000;

    @Test
    public void randomTest() {
        Random random = new Random(System.currentTimeMillis());

        int sum = 0;
        for(int i = 0; i < NUMBER_DATA; i++) {
            int randomNumber = random.nextInt();
            randomNumber = randomNumber < 0 ? -randomNumber : randomNumber;
            randomNumber %= 100;

            sum += randomNumber;
        }

        int average = sum / NUMBER_DATA;
        System.out.println("average == " + average);
    }

    @Test
    public void test1() {
        System.out.println("failure rate 1% ");
        this.testRun(1);
    }

    @Test
    public void test10() {
        System.out.println("failure rate 10% ");
        this.testRun(10);
    }

    @Test
    public void test50() {
        System.out.println("failure rate 50% ");
        this.testRun(50);
    }

    @Test
    public void test99() {
        System.out.println("failure rate 99% ");
        this.testRun(99);
    }

    public void testRun(int byteFailureRate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // fill with some data
        for(int i = 0; i < NUMBER_DATA; i++) {
            baos.write(i);
        }

        // test failure
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        // wrap it
        InputStream is = new InputFailureSimulatorWrapper(bais, byteFailureRate);

        int failureTotalNumber = 0;
        for(int i = 0; i < NUMBER_DATA; i++) {
            try {
                int value = is.read();
                while(value != i) {
                    failureTotalNumber++;
                    i++;
                    if(i == NUMBER_DATA) break;
                } // in sync again
            } catch (IOException e) {
                System.out.println("IOException at i == " + i);
                break;
            }
        }

        System.out.println("failure: " + failureTotalNumber + " / " + NUMBER_DATA);
    }
}
