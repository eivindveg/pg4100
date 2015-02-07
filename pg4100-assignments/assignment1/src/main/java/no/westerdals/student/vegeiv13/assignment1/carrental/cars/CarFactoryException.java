package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

/**
 * Thrown when a CarFactory is attempted set up improperly
 */
public class CarFactoryException extends RuntimeException {
    public CarFactoryException(String message) {
        super(message);
    }
}
