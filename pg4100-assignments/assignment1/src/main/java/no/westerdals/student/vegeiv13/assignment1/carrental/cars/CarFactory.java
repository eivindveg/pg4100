package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarFactory {

    private final String prefix;
    private final int numbers;

    public CarFactory(String prefix, int numbersLength) {
        this.numbers = numbersLength;
        if (prefix == null || prefix.length() != 2) {
            throw new CarFactoryException("Prefix length must be two letters");
        }
        if(numbersLength < 2) {
            throw new CarFactoryException("Plate number length must be at least 2");
        }
        this.prefix = prefix.toUpperCase();
    }

    public RentalCar createRentalCar() {
        final Random random = new Random();
        final StringBuilder plateBuilder = new StringBuilder(getPrefix());
        for (int i = 0; i < numbers; i++) {
            plateBuilder.append(random.nextInt(9));
        }
        return new RentalCar(plateBuilder.toString());
    }

    public List<RentalCar> createRentalCars(int n) {
        final List<RentalCar> cars = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cars.add(createRentalCar());
        }
        return cars;
    }

    public String getPrefix() {
        return prefix;
    }
}
