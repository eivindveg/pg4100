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
    @FXMLViewContext
    public ViewContext context;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label label;
    private Client client;
    private CarRental carRental;
    private Phaser phaser;

    /**
     * A PostConstruct method. This method makes sure the controller/service hybrid is ready to represent a client
     * renting and returning cars
     *
     * @param name   name of the client object to set up
     * @param phaser the phaser to use in ClientTasks.
     */
    public void bind(final String name, final Phaser phaser) {
        this.phaser = phaser;
        client = new Client(name);
        carRental = context.getRegisteredObject(CarRental.class);
        label.textProperty().bind(getClient().nameProperty());
        progress.progressProperty().bind(this.progressProperty());
    }

    /**
     * Creates a ClientTask depending on this service's state
     *
     * @return a client task that is ready to execute immediately
     */
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

    /**
     * Creates a random sleep duration within the min/max bounds
     *
     * @param min the minimum duration to sleep
     * @param max the maximum duration to sleep
     * @return a pseudorandom sleep duration
     */
    private Integer getSleepDuration(int min, int max) {
        Random r = new Random();
        int i = r.nextInt(max - min);
        return i + min;
    }

    /**
     * Creates a renting task. That is a task that has rented a car and will wait until it should be returned
     *
     * @return a renting task
     */
    private RentingTask createRentingTask() {
        return new RentingTask(getClient(), carRental, phaser, getSleepDuration(MIN_SLEEP_DURATION, MAX_SLEEP_DURATION_RENTING));
    }

    /**
     * Creates a ready task. That is a task that sleeps before transitioning to waiting
     *
     * @return a ready task
     */
    private ReadyTask createReadyTask() {
        return new ReadyTask(getClient(), carRental, phaser, getSleepDuration(MIN_SLEEP_DURATION, MAX_SLEEP_DURATION_READY));
    }

    /**
     * Creates a waiting task. That is a task that waits until it can rent a car
     *
     * @return a waiting task
     */
    private WaitingTask createWaitingTask() {
        return new WaitingTask(getClient(), carRental, phaser);
    }

    /**
     * Gets this service's client object
     *
     * @return this service's client object
     */
    public Client getClient() {
        return client;
    }
}
