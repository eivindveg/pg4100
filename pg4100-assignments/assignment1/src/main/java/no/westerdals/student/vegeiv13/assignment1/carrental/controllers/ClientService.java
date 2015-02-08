package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent.ReadyTask;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent.RentingTask;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent.WaitingTask;
import org.datafx.controller.FXMLController;
import org.datafx.controller.context.FXMLViewContext;
import org.datafx.controller.context.ViewContext;

import java.util.Random;
import java.util.concurrent.Phaser;

@FXMLController("/actor.fxml")
public class ClientService extends Service<ClientState> {

    public static final int MIN_SLEEP_DURATION = 1000;
    public static final int MAX_SLEEP_DURATION_READY = 10000;
    private static final int MAX_SLEEP_DURATION_RENTING = 3000;

    @FXML
    private ProgressBar progress;
    @FXML
    private Label label;

    private Client client;

    @FXMLViewContext
    public ViewContext context;

    private CarRental carRental;
    private Phaser phaser;

    public void bind(final String name, final Phaser phaser) {
        this.phaser = phaser;
        client = new Client(name);
        carRental = context.getRegisteredObject(CarRental.class);
        label.textProperty().bind(getClient().nameProperty());
        progress.progressProperty().bind(this.progressProperty());
    }

    @Override
    protected Task<ClientState> createTask() {
        final Task<ClientState> task;
        switch (client.getClientState()) {
            case READY:
                task = createReadyTask();
                break;
            case WAITING:
                task = createWaitingTask();
                break;
            case RENTING:
                task = createRentingTask();
                break;
            default:
                return null;
        }
        task.setOnSucceeded(event -> {
            getClient().setClientState(task.getValue());
            restart();
        });
        return task;
    }

    private Integer getSleepDuration(int min, int max) {
            Random r = new Random();
            int i = r.nextInt(max - min);
            return i + min;
    }

    private RentingTask createRentingTask() {
        return new RentingTask(getClient(), carRental, phaser, getSleepDuration(MIN_SLEEP_DURATION, MAX_SLEEP_DURATION_RENTING));
    }

    private ReadyTask createReadyTask() {
        return new ReadyTask(getClient(), carRental, phaser, getSleepDuration(MIN_SLEEP_DURATION, MAX_SLEEP_DURATION_READY));
    }

    private WaitingTask createWaitingTask() {
        return new WaitingTask(getClient(), carRental, phaser);
    }

    public Client getClient() {
        return client;
    }
}
