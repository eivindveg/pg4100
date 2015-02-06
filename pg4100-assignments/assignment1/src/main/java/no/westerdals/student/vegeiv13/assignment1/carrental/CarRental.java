package no.westerdals.student.vegeiv13.assignment1.carrental;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarFactory;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CarRental {

    private static CarRental instance;
    private final List<RentalCar> rentalCars = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition carReady = lock.newCondition();

    private CarRental() {
        CarFactory carFactory = new CarFactory("UF", 5);
        rentalCars.addAll(carFactory.createRentalCars(3));
    }

    public synchronized static CarRental getInstance() {
        if (instance == null) {
            instance = new CarRental();
        }
        return instance;
    }

    public Condition getCarReadyCondition() {
        return carReady;
    }

    public RentalCar rentCar(Client client) {
        try {
            Optional<RentalCar> rentalCarOptional;
            while (!(rentalCarOptional = rentalCars.stream()
                    .filter(e -> !e.isRented())
                    .findFirst())
                    .isPresent()) {
                System.out.println("Awaiting");
                carReady.await();
            }
            System.out.println("Done waiting");
            RentalCar rentalCar = rentalCarOptional.get();
            rentalCar.setRentedBy(client);
            System.out.println(rentalCar);
            return rentalCar;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void returnCarByClient(final Client client) {
        lock.lock();
        try {
            rentalCars.stream().filter(c -> c.getRentedBy().equals(client)).forEach(c -> c.setRentedBy(null));
            carReady.signalAll();
        } finally {
            lock.unlock();
        }
    }

}
