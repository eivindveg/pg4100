package no.westerdals.student.vegeiv13.io.sockets.client.superchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private final ServerSocket socket;
    private final ExecutorService executorService;
    private boolean running = true;
    private final List<ClientConnection> clients = new CopyOnWriteArrayList<>();

    public Server() throws IOException {
        socket = new ServerSocket(20301);
        executorService = Executors.newCachedThreadPool();
    }

    public void start() throws IOException {
        while(running) {
            Socket accept = socket.accept();
            ClientConnection connection = new ClientConnection(accept, clients);
            executorService.execute(connection);
            clients.add(connection);
        }

        List<Runnable> runnables = executorService.shutdownNow();
        for (final Runnable runnable : runnables) {
            try {
                ((ClientConnection) runnable).close();
            } catch(Exception ignored) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    @Override
    public void run() {
        try {
            start();
        } catch (IOException e) {
            running = false;
        }
    }
}
