package networks.utils;

import org.junit.jupiter.api.Assertions;
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
        Assertions.assertTrue(this.testRun(10) > 0.005);
    }

    @Test
    public void test10() {
        System.out.println("failure rate 10% ");
        Assertions.assertTrue(this.testRun(10) > 0.08);
    }

    @Test
    public void test50() {
        System.out.println("failure rate 50% ");
        Assertions.assertTrue(this.testRun(50) > 0.4);
    }

    @Test
    public void test99() {
        System.out.println("failure rate 99% ");
        Assertions.assertTrue(this.testRun(99) > 0.9);
    }

    public double testRun(int byteFailureRate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // fill with some data
        for(int i = 0; i < NUMBER_DATA; i++) {
            baos.write(i);
        }

        // test failure
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        // wrap it
        InputStream is = new InputStreamFailureSimulator(byteFailureRate, true, bais);

        int failureTotalNumber = 0;
        for(int expectedInt = 0; expectedInt < NUMBER_DATA; expectedInt++) {
            try {
                int value = is.read();
                if(value != expectedInt) failureTotalNumber++;
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                break;
            }
        }

        System.out.println("failure: " + failureTotalNumber + " / " + NUMBER_DATA);
        return failureTotalNumber / NUMBER_DATA;
    }
}
