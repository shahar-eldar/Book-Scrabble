package test;

import java.io.*;
import java.util.Arrays;

public class BookScrabbleHandler implements ClientHandler {

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outToClient), true);
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
        // Nothing specific to close for now
    }
}
