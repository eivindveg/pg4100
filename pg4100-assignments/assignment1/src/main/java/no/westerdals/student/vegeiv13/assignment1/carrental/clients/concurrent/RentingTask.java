package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.Phaser;

public class RentingTask extends ClientTask {


    private Integer sleepDuration;

    /**
     * Sets up a renting task that sleeps before attempting to return its client's car
     *
     * @param sleepDuration How long this task should sleep before returning the car
     */
    public RentingTask(final Client client, final CarRental carRental, final Phaser phaser, final Integer sleepDuration) {
        super(client, carRental, phaser);
        this.sleepDuration = sleepDuration;
    }

    /**
     * Sleeps for a certain duration while updating its progress, then attempts to return the car to the rental
     *
     * @return ClientState.READY
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
        getCarRental().returnCarByClient(getClient());
        return ClientState.READY;
    }

    /**
     * Gets this task's sleep duration
     *
     * @return this task's sleep duration
     */
    private Integer getSleepDuration() {
        return sleepDuration;
    }
}
