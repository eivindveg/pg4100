package no.westerdals.student.vegeiv13.io.sockets.client.superchat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements AutoCloseable {

    private final Listener listener;
    private final Transmitter transmitter;
    private Socket socket;

    public Client(final Socket socket, final String nickname) throws IOException {
        this.socket = socket;
        listener = new Listener(socket.getInputStream());
        transmitter = new Transmitter(socket.getOutputStream(), nickname);
        new Thread(listener).start();
        new Thread(transmitter).start();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(new Socket(InetAddress.getLocalHost(), 20301), "Eivind");
    }

    @Override
    public void close() throws Exception {
        listener.close();
        transmitter.close();
        socket.close();
    }

    public class Listener implements Runnable, Closeable {

        private final DataInputStream in;

        public Listener(final InputStream inputStream) {
            in = new DataInputStream(inputStream);
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readUTF()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                try {
                    Client.this.close();
                } catch (Exception ignored) {
                }
            }
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }

    public class Transmitter implements Runnable, Closeable {

        private final DataOutputStream out;

        public Transmitter(final OutputStream outputStream, final String nickname) throws IOException {
            out = new DataOutputStream(outputStream);
            out.writeUTF(nickname);
            out.flush();
        }

        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    out.writeUTF(message);
                }
            } catch (IOException e) {
                try {
                    Client.this.close();
                } catch (Exception ignored) {

                }
            }
        }

        @Override
        public void close() throws IOException {
            out.close();
        }
    }
}
