package algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ResponseReader implements AutoCloseable {
    private BufferedReader reader;

    public ResponseReader(String fileName) {
        try {
            reader = new BufferedReader(new FileReader(fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() {
        try {
            return reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
