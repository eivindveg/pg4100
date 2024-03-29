package no.westerdals.student.vegeiv13.concurrency;

import no.westerdals.student.vegeiv13.io.FileHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PrimeHandler {

    public static final int THREADS = 20;
    public static final String DEFAULT_FILE = "generated/primes.txt";

    private long lastChecked;
    private ExecutorService service;
    private List<Future<Long>> futures = new CopyOnWriteArrayList<>();

    public PrimeHandler() {
        this(0);
    }

    public PrimeHandler(long startValue) {
        if (startValue < 0) {
            throw new UnsupportedOperationException("Start value must be positive");
        } else {
            lastChecked = startValue - 1;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PrimeHandler handler;
        FileHandler fileHandler;
        fileHandler = new FileHandler(DEFAULT_FILE);
        long value;
        String lastLine = fileHandler.getLastLine();
        try {
            value = Long.parseLong(lastLine);
        } catch (NumberFormatException e) {
            value = 0;
        }
        for (long i = value; i < Long.MAX_VALUE; i = handler.getLastChecked()) {
            handler = new PrimeHandler(i);
            handler.run();
            while (handler.isRunning()) {
                Thread.sleep(1);
            }
            fileHandler.appendLinesConcurrently(flush(handler.getFutures()));
        }
    }

    public static List<Long> flush(List<Future<Long>> futures) {
        List<Long> values = new ArrayList<>();
        futures.forEach(f -> {
            long value = invokeSilent(f);
            if (value >= 0) {
                System.out.println(value);
                values.add(value);
            }
        });
        return values;
    }

    private static long invokeSilent(final Future<Long> f) {
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }

    public List<Future<Long>> getFutures() {
        return futures;
    }

    public void run() {
        service = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < 1000; i++) {
            futures.add(service.submit(new PrimeSeeker(++lastChecked)));
        }
        service.shutdown();
    }

    public boolean isRunning() {
        return !service.isTerminated();
    }

    public long getLastChecked() {
        return lastChecked;
    }
}
