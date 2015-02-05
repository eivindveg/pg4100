package no.westerdals.student.vegeiv13.io.sockets.client;

import java.io.*;
import java.net.Socket;

public class Client implements AutoCloseable {

    public static final String DEFAULT_HOST = "10.21.11.125";
    public static int DEFAULT_PORT = 8000;
    private final Socket socket;
    private ObjectInputStream inputStream;
    private DataOutputStream outputStream;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try (Client c = new Client(DEFAULT_HOST, DEFAULT_PORT)) {
            System.out.println(c.connect());

            String message;
            while (!(message = reader.readLine()).equals("exit")) {
                System.out.println(c.send(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String connect() throws IOException {
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        return inputStream.readUTF();
    }

    public String send(String message) throws IOException {
        try {
            outputStream.writeUTF(message);
            return inputStream.readUTF();
        } finally {
            outputStream.flush();
        }

    }

    @Override
    public void close() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }
}
