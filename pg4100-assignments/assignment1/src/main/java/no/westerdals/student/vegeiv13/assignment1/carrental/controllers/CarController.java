package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import org.datafx.controller.FXMLController;

@FXMLController("/car.fxml")
public class CarController {

    @FXML
    private Label clientLabel;
    @FXML
    private Label carLabel;

    /**
     * Gets this car controller's client label
     * @return a label representing the current client's name, TEXT WILL CHANGE UPON RENTING
     */
    public Label getClientLabel() {
        return clientLabel;
    }

    /**
     * Gets this car controller's car label
     * @return a label bound to the numberplate property of this controller's car
     */
    public Label getCarLabel() {
        return carLabel;
    }

    /**
     * Binds this controller to the given car, making sure the label properties match at any given time
     * @param car The car to bind to
     */
    public void bind(RentalCar car) {
        if(car.isRented()) {
            bindToClient(car.getRentedBy());
        }

        car.clientProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(final ObservableValue<? extends Client> observable, final Client oldValue, final Client newValue) {
                if (newValue != null) {
                    Platform.runLater(() -> bindToClient(newValue));
                } else {
                    Platform.runLater(() -> {
                        clientLabel.textProperty().unbind();
                        clientLabel.setText("Available");
                    });
                }
            }
        });
        carLabel.textProperty().bind(car.registrationNumberProperty());
    }

    /**
     * Binds the clientlabel to the name of the new client
     * @param client the new client
     */
    private void bindToClient(final Client client) {
        clientLabel.textProperty().bind(client.nameProperty());
    }
}
