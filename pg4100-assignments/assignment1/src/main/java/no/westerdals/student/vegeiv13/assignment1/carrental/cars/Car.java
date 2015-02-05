package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

public abstract class Car {

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    private final String registrationNumber;

    protected Car(final String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
