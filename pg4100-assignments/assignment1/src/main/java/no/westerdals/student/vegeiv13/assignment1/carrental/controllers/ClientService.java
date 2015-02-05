package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;
import org.datafx.controller.FXMLController;

import java.util.concurrent.Phaser;

@FXMLController("/actor.fxml")
public class ClientService extends Service<Void> {

    private static final Phaser phaser = new Phaser(5);
    private static final int SLEEP_DURATION_MILLIS = 9000;
    private static final int RENT_DURATION_MILLIS = 2000;
    private final CarRental carRental = CarRental.getInstance();


    ClientState state = ClientState.READY;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label label;
    private RentalCar rentalCar;
    private Client client;

    public ClientState getClientState() {
        return state;
    }

    public void bind(final String name) {
        client = new Client(name);
        label.textProperty().bind(client.nameProperty());
        progress.progressProperty().bind(this.progressProperty());
    }

    @Override
    protected Task<Void> createTask() {
        switch(state) {
            case READY: return createReadyTask();
            case WAITING: return createWaitingTask();
            case RENTING: return createRentingTask();
            default:
                return null;
        }
    }

    private Task<Void> createReadyTask() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                long startStamp = System.currentTimeMillis();
                long diff = 0;
                while (diff < SLEEP_DURATION_MILLIS) {
                    diff = System.currentTimeMillis() - startStamp;
                    updateProgress(diff, SLEEP_DURATION_MILLIS);
                    Thread.yield();
                }
                state = ClientState.WAITING;
                return null;
            }


        };
    }

    private Task<Void> createWaitingTask() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                // TODO LOCK AND WAIT FOR AVAILABLE CAR
                updateProgress(ProgressBar.INDETERMINATE_PROGRESS, ProgressBar.INDETERMINATE_PROGRESS);
                System.out.println(getClient());
                rentalCar = carRental.rentCar(getClient());
                state = ClientState.RENTING;
                return null;
            }
        };
    }

    private Task<Void> createRentingTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long startStamp = System.currentTimeMillis();
                long diff = 0;
                while (diff < RENT_DURATION_MILLIS) {
                    diff = System.currentTimeMillis() - startStamp;
                    updateProgress(diff, RENT_DURATION_MILLIS);
                    Thread.yield();
                }
                carRental.returnCar(rentalCar);
                state = ClientState.READY;
                return null;
            }
        };
    }

    public void setClientState(final ClientState clientState) {
        this.state = clientState;
    }

    public RentalCar getRentalCar() {
        return rentalCar;
    }

    public void setRentalCar(final RentalCar rentalCar) {
        this.rentalCar = rentalCar;
    }

    public Client getClient() {
        return client;
    }
}
