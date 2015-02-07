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

    public CarRental(String prefix) {
        carFactory = new CarFactory(prefix, 5);
        rentalCars = Collections.synchronizedList(new ArrayList<>());
        rentalCars.addAll(carFactory.createRentalCars(3));
    }

    public List<RentalCar> getRentalCarsUnmodifiable() {
        return Collections.unmodifiableList(rentalCars);
    }

    public RentalCar rentCar(Client client) {
        lock.lock();
        try {
            Optional<RentalCar> rentalCarOptional;
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
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public synchronized void returnCarByClient(final Client client) {
        if (!lock.isHeldByCurrentThread()) {
            lock.lock();
        }
        try {
            rentalCars.stream().filter(rentalCar -> rentalCar.isRented() && rentalCar.getRentedBy().equals(client)).forEach(rentalCar -> rentalCar.setRentedBy(null));
            carReady.signal();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public RentalCar addNewCar() {
        RentalCar rentalCar = carFactory.createRentalCar();
        if(rentalCars.add(rentalCar)) {
            return rentalCar;
        }
        return null;
    }
}
