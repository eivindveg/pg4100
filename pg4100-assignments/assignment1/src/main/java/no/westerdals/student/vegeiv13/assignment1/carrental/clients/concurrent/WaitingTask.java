package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import javafx.scene.control.ProgressBar;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;

public class WaitingTask extends ClientTask {
    public WaitingTask(final Client client, final CarRental carRental) {
        super(client, carRental);
    }

    @Override
    protected ClientState call() throws Exception {
        updateProgress(ProgressBar.INDETERMINATE_PROGRESS, ProgressBar.INDETERMINATE_PROGRESS);
        getCarRental().rentCar(getClient());
        return ClientState.RENTING;
    }
}
