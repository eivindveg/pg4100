package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class CarRentalTest {

    private CarRental carRental;
    private Client client;

    @Before
    public void setup() throws Exception {
        carRental = new CarRental("UF", 1);
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

    @Test
    public void testRentCar() {
        RentalCar car = carRental.rentCar(client);
        assertNotNull("We could rent a car", car);
        carRental.returnCarByClient(client);
    }

    @Test
    public void testAddCar() {
        RentalCar rentalCar = carRental.addNewCar();
        assertNotNull("Adding a new car does not return false", rentalCar);
    }

    @Test
    public void testRentCarCanBeInterrupted() {
        final RentalCar[] car = new RentalCar[1];
        Thread t = new Thread(() -> {
           car[0] = carRental.rentCar(client);
        });
        t.start();
        t.interrupt();
        assertNull("We did not get a car as we interrupted the thread", car[0]);
    }

    @Test
    public void testRentCarConcurrentlyBlocks() throws Exception {
        List<Thread> threads = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            Client client = new Client("TestClient" + i);
            clients.add(client);
            threads.add(new Thread(() -> {
                carRental.rentCar(client);
            }));
        }
        threads.forEach(Thread::start);
        Thread.sleep(1);
        boolean atLeastOneThreadHasNotFinished = threads.stream().anyMatch(Thread::isAlive);
        assertTrue("At least one thread has not finished", atLeastOneThreadHasNotFinished);
        threads.forEach(Thread::interrupt);
        clients.forEach(carRental::returnCarByClient);
    }


}
