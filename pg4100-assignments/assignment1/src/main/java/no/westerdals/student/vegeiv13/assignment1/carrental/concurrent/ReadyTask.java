package no.westerdals.student.vegeiv13.assignment1.carrental.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;

public class ReadyTask extends ClientTask {
    public ReadyTask(final Client client, final CarRental carRental) {
        super(client, carRental, ClientState.READY);
    }

    @Override
    protected ClientState call() throws Exception {
        long startStamp = System.currentTimeMillis();
        long diff = 0;
        while (diff < SLEEP_DURATION_MILLIS) {
            diff = System.currentTimeMillis() - startStamp;
            updateProgress(diff, SLEEP_DURATION_MILLIS);
            Thread.yield();
        }
        return ClientState.WAITING;
    }
}
