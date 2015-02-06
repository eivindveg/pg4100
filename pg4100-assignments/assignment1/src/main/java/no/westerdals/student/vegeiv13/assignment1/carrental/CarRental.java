package no.westerdals.student.vegeiv13.assignment1.carrental;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarFactory;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CarRental {

    private static CarRental instance;
    private final List<RentalCar> rentalCars;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition carReady = lock.newCondition();

    private CarRental() {
        CarFactory carFactory = new CarFactory("UF", 5);
        rentalCars = Collections.synchronizedList(new ArrayList<>());
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
        lock.lock();
        try {
            Optional<RentalCar> rentalCarOptional;
            while (!(rentalCarOptional = rentalCars.stream()
                    .filter(e -> !e.isRented())
                    .findFirst()
            ).isPresent()) {
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

    public synchronized void returnCarByClient(final Client client) {
        if(!lock.isHeldByCurrentThread()) {
            lock.lock();
        }
        try {
            System.out.println("Returning car for client " + client);
            rentalCars.stream().filter(rentalCar -> rentalCar.isRented() && rentalCar.getRentedBy().equals(client)).forEach(rentalCar -> rentalCar.setRentedBy(null));
            System.out.println("Retrieved for client " + client);
            carReady.signal();
            System.out.println("Signalled for client " + client);
        } finally {
            if(lock.isHeldByCurrentThread()) {
                System.out.println("Unlocking for client " + client);
                lock.unlock();
                System.out.println("Unlocked for client " + client);
                System.out.println(rentalCars);
            }
        }
    }

}
