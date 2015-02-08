package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.Phaser;

public abstract class ClientTask extends Task<ClientState> {

    private final Client client;
    private final CarRental carRental;
    private final Phaser phaser;

    public ClientTask(final Client client, final CarRental carRental, Phaser phaser) {
        this.client = client;
        this.carRental = carRental;
        this.phaser = phaser;
    }

    protected Client getClient() {
        return client;
    }

    protected CarRental getCarRental() {
        return carRental;
    }

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();
        super.run();
    }
}
