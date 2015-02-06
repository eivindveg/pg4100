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

    public void bind(RentalCar car) {
        car.clientProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(final ObservableValue<? extends Client> observable, final Client oldValue, final Client newValue) {
                if (newValue != null) {
                    Platform.runLater(() -> clientLabel.textProperty().bind(newValue.nameProperty()));
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
}
