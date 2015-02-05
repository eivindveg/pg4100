package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarFactory {

    private final String prefix;
    private final int numbers;

    public CarFactory(String prefix, int numbers) {
        this.numbers = numbers;
        if (prefix == null || prefix.length() != 2) {
            throw new CarFactoryException("Prefix length must be two letters");
        }
        this.prefix = prefix.toUpperCase();
    }

    public RentalCar createRentalCar() {
        Random random = new Random();
        StringBuilder plateBuilder = new StringBuilder(getPrefix());
        for (int i = 0; i < numbers; i++) {
            plateBuilder.append(random.nextInt(9));
        }
        return new RentalCar(plateBuilder.toString());
    }

    public List<RentalCar> createRentalCars(int n) {
        List<RentalCar> cars = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cars.add(createRentalCar());
        }
        return cars;
    }

    public String getPrefix() {
        return prefix;
    }
}
