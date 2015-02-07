package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CarFactory objects are used to generate cars without having to pass arguments to a car's constructor
 */
public class CarFactory {

    private final String prefix;
    private final int numbers;

    /**
     * Creates a CarFactory
     * @param prefix the prefix for the number plates
     * @param numbersLength the numeric length of number plates
     */
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

    /**
     * Creates a single rental car with a random number plate
     * @return the newly created Rental Car
     */
    public RentalCar createRentalCar() {
        String registrationNumber = createNumberPlate();
        return new RentalCar(registrationNumber);
    }

    /**
     * Builds a number plate with this factory's prefix and the assigned number plate length
     * @return the generated number plate
     */
    private String createNumberPlate() {
        final Random random = new Random();
        final StringBuilder plateBuilder = new StringBuilder(getPrefix());
        for (int i = 0; i < numbers; i++) {
            plateBuilder.append(random.nextInt(9));
        }
        return plateBuilder.toString();
    }

    /**
     * Creates n rental cars with random number plates
     * @param n the amount of cars to create
     * @return a list containing the created cars
     */
    public List<RentalCar> createRentalCars(int n) {
        final List<RentalCar> cars = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            cars.add(createRentalCar());
        }
        return cars;
    }

    /**
     * Gets this factory's prefix
     * @return this factory's prefix
     */
    public String getPrefix() {
        return prefix;
    }
}
