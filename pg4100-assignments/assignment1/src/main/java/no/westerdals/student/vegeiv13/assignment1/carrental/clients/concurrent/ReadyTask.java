package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.Phaser;

public class ReadyTask extends ClientTask {


    private Integer sleepDuration;

    public ReadyTask(final Client client, final CarRental carRental, final Phaser phaser, final Integer sleepDuration) {
        super(client, carRental, phaser);
        this.sleepDuration = sleepDuration;
    }


    @Override
    protected ClientState call() throws Exception {
        long startStamp = System.currentTimeMillis();
        long diff = 0;
        while (diff < getSleepDuration()) {
            diff = System.currentTimeMillis() - startStamp;
            updateProgress(diff, getSleepDuration());
            Thread.sleep(1L);
        }
        return ClientState.WAITING;
    }

    public Integer getSleepDuration() {
        return sleepDuration;
    }
}
