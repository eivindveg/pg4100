package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

public class CarFactory {

    private final String prefix;

    public CarFactory(String prefix) {
        if (prefix == null || prefix.length() != 2) {
            throw new CarFactoryException("Prefix length must be two letters");
        }
        this.prefix = prefix.toUpperCase();
    }

    public String getPrefix() {
        return prefix;
    }
}
