package no.westerdals.student.vegeiv13.io.sockets.client.superchat.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class ClientConnection implements Closeable, Runnable {

    private final DataOutputStream out;
    private final DataInputStream in;
    private String name;
    private List<ClientConnection> pool;
    private boolean running = true;

    public ClientConnection(final Socket accept, List<ClientConnection> pool) throws IOException {
        this.pool = pool;
        System.out.println(accept.getInetAddress() + " connected");
        in = new DataInputStream(accept.getInputStream());
        out = new DataOutputStream(accept.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        System.out.println("Disconnecting " + name);
        try {
            in.close();
            out.close();
        } finally {
            running = false;
            pool.remove(this);
        }
    }

    @Override
    public void run() {
        try {
            name = in.readUTF();
            System.out.println(name + " registered");
            out.writeUTF("Your name is set to: " + name);
            out.flush();
            while (running) {
                final String message = in.readUTF();
                pool.forEach(e -> e.sendMessage(message, name));
            }
        } catch (IOException e) {
            try {
                close();
            } catch (IOException ignored) {
            }
        }
    }

    public void sendMessage(String message, String name) {
        message = wrap(message, name);
        try {
            Logger.getInstance().write(message);
            out.writeUTF(message);
            System.out.println(message);
            out.flush();
        } catch (IOException e) {
            try {
                this.close();
            } catch (IOException ignored) {
            }
        }

    }

    private String wrap(final String message, String name) {
        StringBuilder builder = new StringBuilder();
        String stamp = LocalDateTime.now().toString();
        return builder
                .append("[")
                .append(stamp)
                .append("]")
                .append("[")
                .append(name)
                .append("]")
                .append(": ")
                .append(message).toString();
    }
}
