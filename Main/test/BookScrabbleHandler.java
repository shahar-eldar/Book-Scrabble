package test;

import java.io.*;
import java.util.Arrays;

public class BookScrabbleHandler implements ClientHandler {
    private BufferedReader reader;
    private PrintWriter writer;

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            reader = new BufferedReader(new InputStreamReader(inFromClient));
            writer = new PrintWriter(new OutputStreamWriter(outToClient), true);
            String[] inputs = reader.readLine().split(",");
            boolean result = false;

            if (inputs[0].equals("Q")) {
                result = DictionaryManager.get().query(Arrays.copyOfRange(inputs, 1, inputs.length));
            } else if (inputs[0].equals("C")) {
                result = DictionaryManager.get().challenge(Arrays.copyOfRange(inputs, 1, inputs.length));
            }

            writer.println(result ? "true" : "false");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
