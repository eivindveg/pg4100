package no.westerdals.student.vegeiv13.assignment1.carrental.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;

public class RentingTask extends ClientTask {
    public RentingTask(final Client client, final CarRental carRental) {
        super(client, carRental, ClientState.RENTING);
    }

    @Override
    protected ClientState call() throws Exception {
        long startStamp = System.currentTimeMillis();
        long diff = 0;
        while (diff < RENT_DURATION_MILLIS) {
            diff = System.currentTimeMillis() - startStamp;
            updateProgress(diff, RENT_DURATION_MILLIS);
            Thread.yield();
        }
        getCarRental().returnCarByClient(getClient());
        return ClientState.READY;
    }
}
