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
import no.westerdals.student.vegeiv13.assignment1.carrental.concurrent.ReadyTask;
import no.westerdals.student.vegeiv13.assignment1.carrental.concurrent.RentingTask;
import no.westerdals.student.vegeiv13.assignment1.carrental.concurrent.WaitingTask;
import org.datafx.controller.FXMLController;

@FXMLController("/actor.fxml")
public class ClientService extends Service<ClientState> {


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
    protected Task<ClientState> createTask() {
        System.out.println("Restarting controller with state: " + state);
        Task<ClientState> task;
        switch(state) {
            case READY: task = createReadyTask();
                break;
            case WAITING: task = createWaitingTask();
                break;
            case RENTING: task = createRentingTask();
                break;
            default:
                return null;
        }
        task.setOnSucceeded(event -> {
            state = task.getValue();
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
