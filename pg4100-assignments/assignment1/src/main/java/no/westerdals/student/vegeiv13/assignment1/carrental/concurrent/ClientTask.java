package no.westerdals.student.vegeiv13.assignment1.carrental.concurrent;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;

public class ClientTask extends Task<ClientState> {

    public ClientState getClientState() {
        return state;
    }

    ClientState state = ClientState.READY;

    private static final int SLEEP_DURATION_MILLIS = 9000;
    private static final int RENT_DURATION_MILLIS = 2000;

    @Override
    protected ClientState call() throws Exception {
        long startStamp = System.currentTimeMillis();
        long diff = 0;
        switch (state) {
            case READY:
                while (diff < SLEEP_DURATION_MILLIS) {
                    diff = System.currentTimeMillis() - startStamp;
                    updateProgress(diff, SLEEP_DURATION_MILLIS);
                    Thread.sleep(20);
                }
                state = ClientState.WAITING;
                break;
            case WAITING:
                // TODO LOCK AND WAIT FOR AVAILABLE CAR
                updateProgress(ProgressBar.INDETERMINATE_PROGRESS, 0);
                break;
            case RENTING:
                while(diff < RENT_DURATION_MILLIS) {
                    diff = System.currentTimeMillis() - startStamp;
                    updateProgress(diff, RENT_DURATION_MILLIS);
                    Thread.sleep(20);
                }
                break;
        }
        return state;
    }
}
