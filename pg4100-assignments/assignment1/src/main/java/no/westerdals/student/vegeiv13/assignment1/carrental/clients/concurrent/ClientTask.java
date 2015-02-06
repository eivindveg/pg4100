package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.CountDownLatch;

public abstract class ClientTask extends Task<ClientState> {

    private static final CountDownLatch latch = new CountDownLatch(5);
    private final Client client;
    private final CarRental carRental;
    private final ClientState clientState;

    public ClientTask(final Client client, final CarRental carRental, ClientState clientState) {
        this.client = client;
        this.carRental = carRental;
        this.clientState = clientState;
    }

    protected Client getClient() {
        return client;
    }

    protected CarRental getCarRental() {
        return carRental;
    }

    protected ClientState getClientState() {
        return clientState;
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
