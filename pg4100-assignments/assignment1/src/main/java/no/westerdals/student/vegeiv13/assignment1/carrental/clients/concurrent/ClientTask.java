package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.CountDownLatch;

public abstract class ClientTask extends Task<ClientState> {

    private static final CountDownLatch latch = new CountDownLatch(5);
    private final Client client;
    private final CarRental carRental;

    public ClientTask(final Client client, final CarRental carRental) {
        this.client = client;
        this.carRental = carRental;
    }

    protected Client getClient() {
        return client;
    }

    protected CarRental getCarRental() {
        return carRental;
    }

    @Override
    public void run() {
        latch.countDown();
        try {
            latch.await();
            super.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
