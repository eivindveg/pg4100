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

@FXMLController("/actor.fxml")
public class ClientService extends Service<ClientState> {

    @FXML
    private ProgressBar progress;
    @FXML
    private Label label;

    private Client client;

    @FXMLViewContext
    private ViewContext context;

    private CarRental carRental;

    public void bind(final String name) {
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

    private RentingTask createRentingTask() {
        return new RentingTask(getClient(), carRental);
    }

    private ReadyTask createReadyTask() {
        return new ReadyTask(getClient(), carRental);
    }

    private WaitingTask createWaitingTask() {
        return new WaitingTask(getClient(), carRental);
    }

    public Client getClient() {
        return client;
    }
}
