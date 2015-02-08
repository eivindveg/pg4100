package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rules.JavaFXThreadingRule;

import static junit.framework.Assert.assertTrue;

public class CarControllerTest {

    @Rule
    public JavaFXThreadingRule rule = new JavaFXThreadingRule();

    private CarController controller;
    private Client client;
    private RentalCar rentalCar;

    @Before
    public void setup() throws Exception {
        client = new Client("TestClient");
        final CarRental rental = new CarRental("AA", 1);
        rentalCar = rental.rentCar(client);
        ViewContext<CarController> context = ViewFactory.getInstance().createByController(CarController.class);
        controller = context.getController();
        controller.bind(rentalCar);
    }

    @Test
    public void testControllerBindsToClientAndCar() {
        String actual = controller.getCarLabel().getText();
        String expected = rentalCar.registrationNumberProperty().get();
        assertTrue("The text of the car label matches the plates", actual.equals(expected));

        actual = controller.getClientLabel().getText();
        expected = client.nameProperty().get();
        assertTrue("The text of the client label matches the client's name", actual.equals(expected));
    }
}
