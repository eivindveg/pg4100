package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CarRentalTest {

    private CarRental carRental;
    private Client client;

    @Before
    public void setup() throws Exception {
        carRental = new CarRental("UF");
        client = new Client("TestClient");
    }

    @Test
    public void testRentCars() {
        RentalCar rentalCar = carRental.rentCar(client);
        assertNotNull("We could rent a car in a single thread", rentalCar);
        carRental.returnCarByClient(client);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCarRentalExposesUnmodifiableListOnly() {
        carRental.getRentalCarsUnmodifiable().remove(0);
    }


}
