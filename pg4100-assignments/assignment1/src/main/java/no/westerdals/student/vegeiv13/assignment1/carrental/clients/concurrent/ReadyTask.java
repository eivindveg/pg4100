package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.Random;

public class ReadyTask extends ClientTask {

    private static final int MIN_SLEEP_DURATION = 1000;
    private static final int MAX_SLEEP_DURATION = 10000;
    private Integer sleepDuration;

    public ReadyTask(final Client client, final CarRental carRental) {
        super(client, carRental, ClientState.READY);
    }

    private Integer getSleepDuration() {
        if (sleepDuration == null) {
            Random r = new Random();
            int i = r.nextInt(MAX_SLEEP_DURATION - MIN_SLEEP_DURATION);
            sleepDuration = i + MIN_SLEEP_DURATION;
        }
        return sleepDuration;
    }

    @Override
    protected ClientState call() throws Exception {
        long startStamp = System.currentTimeMillis();
        long diff = 0;
        while (diff < getSleepDuration()) {
            diff = System.currentTimeMillis() - startStamp;
            updateProgress(diff, getSleepDuration());
            Thread.yield();
        }
        return ClientState.WAITING;
    }
}
