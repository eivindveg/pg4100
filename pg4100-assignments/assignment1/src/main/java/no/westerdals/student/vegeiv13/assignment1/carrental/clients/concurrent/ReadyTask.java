package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.Phaser;

public class ReadyTask extends ClientTask {

    private Integer sleepDuration;

    /**
     * Sets up a ready task that eventually transitions to waiting
     *
     * @param sleepDuration How long this task should sleep before transitioning
     */
    public ReadyTask(final Client client, final CarRental carRental, final Phaser phaser, final Integer sleepDuration) {
        super(client, carRental, phaser);
        this.sleepDuration = sleepDuration;
    }


    /**
     * Starts this task and makes it wait for the specified duration, whilst updating its progress
     *
     * @return ClientState.WAITING upon completion
     * @throws Exception
     */
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

    /**
     * Gets this task's sleep duration
     *
     * @return this task's sleep duration
     */
    public Integer getSleepDuration() {
        return sleepDuration;
    }
}
