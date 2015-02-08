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

    /**
     * Base constructor for the client tasks. These parameters are considered the bare minimum to fulfill a task
     * @param client The client the task is for
     * @param carRental The rental the client should work on
     * @param phaser The phaser we're supposed to block on
     */
    public ClientTask(final Client client, final CarRental carRental, Phaser phaser) {
        this.client = client;
        this.carRental = carRental;
        this.phaser = phaser;
    }

    /**
     * gets this task's client
     * @return this task's client
     */
    protected Client getClient() {
        return client;
    }

    /**
     * gets this task's car rental
     * @return this task's car rental
     */
    protected CarRental getCarRental() {
        return carRental;
    }

    /**
     * Runs this task. This method is only used by executors, actual task logic should reside in the call method.
     */
    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndDeregister();
        super.run();
    }
}
