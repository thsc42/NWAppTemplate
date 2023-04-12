package examples;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class FileCopyExample {
    public static final String FILE_A_NAME = "exampleFileA.txt";
    public static final String FILE_B_NAME = "exampleFileB.txt";
    public static final String EXAMPLE_STRING = "exampleString";
    public static final int EXAMPLE_INTEGER = 42;

    @Test
    public void fileCopy() throws IOException {
        File fileA = new File(FILE_A_NAME);

        // fill example file with some data - open output stream first
        OutputStream osA = new FileOutputStream(fileA);

        // wrap into data output stream
        DataOutputStream dosA = new DataOutputStream(osA);

        // write example data
        dosA.writeUTF(EXAMPLE_STRING);
        dosA.writeInt(EXAMPLE_INTEGER);

        // close stream
        // dosA.close();
        osA.close();

        File fileB = new File(FILE_B_NAME);
        OutputStream osB = new FileOutputStream(fileB);
        InputStream isA = new FileInputStream(fileA);
        this.copy(osB, isA);
        osB.close();
        isA.close();

        // check result
        InputStream isB = new FileInputStream(fileB);
        DataInputStream disB = new DataInputStream(isB);

        String readString = disB.readUTF();
        Assertions.assertEquals(EXAMPLE_STRING, readString);
        int readInt = disB.readInt();
        Assertions.assertEquals(EXAMPLE_INTEGER, readInt);

    }

    private void copy(OutputStream os, InputStream is) throws IOException {
        while(is.available() > 0) {
            int value = is.read();
            os.write(value);
        }
    }
}
