package no.westerdals.student.vegeiv13.assignment1.carrental.concurrent;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;

import java.util.concurrent.Phaser;

public abstract class ClientTask extends Task<ClientState> {

    protected static final int SLEEP_DURATION_MILLIS = 9000;
    protected static final int RENT_DURATION_MILLIS = 2000;
    private static final Phaser phaser = new Phaser(5);
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
}
