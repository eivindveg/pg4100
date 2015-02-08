package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CarRental {
    private final List<RentalCar> rentalCars;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition carReady = lock.newCondition();
    private final CarFactory carFactory;

    /**
     * Sets up a car rental with the given number plate prefix and number of cars
     *
     * @param prefix      number plate prefix to pass to the factory
     * @param initialCars how many cars to start with
     */
    public CarRental(String prefix, final int initialCars) {
        carFactory = new CarFactory(prefix, 5);
        rentalCars = Collections.synchronizedList(new ArrayList<>());
        rentalCars.addAll(carFactory.createRentalCars(initialCars));
    }

    /**
     * Gets an unmodifiable list of all cars in this rental
     *
     * @return An unmodifiable list of cars
     */
    public List<RentalCar> getRentalCarsUnmodifiable() {
        return Collections.unmodifiableList(rentalCars);
    }

    /**
     * Attempts to rent a car. Blocks until successful or interrupted
     *
     * @param client The client to rent the car to
     * @return The car, or null, that is rented out
     */
    public RentalCar rentCar(Client client) {
        lock.lock();
        try {
            Optional<RentalCar> rentalCarOptional;

            // If there's no car immediately available, wait for the first available car
            while (!(rentalCarOptional = rentalCars.stream()
                    .filter(e -> !e.isRented())
                    .findFirst()
            ).isPresent()) {
                carReady.await();
            }
            RentalCar rentalCar = rentalCarOptional.get();
            rentalCar.setRentedBy(client);
            return rentalCar;
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Attempts to return any car rented by a given client
     *
     * @param client the client to return a car from
     */
    public synchronized void returnCarByClient(final Client client) {
        lock.lock();
        try {
            rentalCars.stream().filter(rentalCar -> rentalCar.isRented() && rentalCar.getRentedBy().equals(client)).forEach(rentalCar -> rentalCar.setRentedBy(null));
            // Signal a single thread that a car is now available
            carReady.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Attempts to add a new car to the car rental
     *
     * @return The newly added car, or null if it failed
     */
    public RentalCar addNewCar() {
        lock.lock();
        try {
            RentalCar rentalCar = carFactory.createRentalCar();
            if (rentalCars.add(rentalCar)) {
                carReady.signal();
                return rentalCar;
            }
            // Should never happen, but assume exceptions are handled elsewhere
            return null;
        } finally {
            lock.unlock();
        }

    }
}
