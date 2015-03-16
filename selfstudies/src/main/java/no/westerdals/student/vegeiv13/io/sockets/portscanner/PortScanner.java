package no.westerdals.student.vegeiv13.io.sockets.portscanner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortScanner {

    private final InetAddress host;

    public PortScanner(final InetAddress host) {
        this.host = host;

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        PortScanner scanner = new PortScanner(InetAddress.getLocalHost());
        System.out.println("Port scanner has detected the following open ports:");
        System.out.println(scanner.getOpenPorts(0, 65535));
    }

    private boolean scan(final int i) {
        try (Socket socket = new Socket(host, i)) {
            return socket.isConnected();
        } catch (IOException e) {
            return false;
        }
    }

    public List<Integer> getOpenPorts(int min, int max) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(1000);
        List<Integer> openPorts = new CopyOnWriteArrayList<>();
        for (int i = min; i <= max; i++) {
            final int j = i;
            service.execute(() -> {
                if (scan(j)) {
                    openPorts.add(j);
                }
            });
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        return openPorts;
    }
}
