package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import javafx.scene.control.ProgressBar;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

import java.util.concurrent.Phaser;

public class WaitingTask extends ClientTask {

    /**
     * Sets up a waiting task
     */
    public WaitingTask(final Client client, final CarRental carRental, final Phaser phaser) {
        super(client, carRental, phaser);
    }

    /**
     * Attempts to rent a car. Uses an indeterminate progress indicator while waiting
     *
     * @return ClientState.RENTING
     * @throws Exception
     */
    @Override
    protected ClientState call() throws Exception {
        updateProgress(ProgressBar.INDETERMINATE_PROGRESS, ProgressBar.INDETERMINATE_PROGRESS);
        getCarRental().rentCar(getClient());
        return ClientState.RENTING;
    }
}
