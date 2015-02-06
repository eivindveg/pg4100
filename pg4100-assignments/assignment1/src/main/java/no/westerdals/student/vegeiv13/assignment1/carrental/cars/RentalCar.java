package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import com.sun.istack.internal.Nullable;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;

public class RentalCar {

    private Client rentedBy;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    private final String registrationNumber;

    protected RentalCar(final String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public synchronized @Nullable Client getRentedBy() {
        return rentedBy;
    }

    @Override
    public String toString() {
        return "RentalCar{" +
                "rentedBy=" + rentedBy +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    public synchronized void setRentedBy(final @Nullable Client rentedBy) {
        this.rentedBy = rentedBy;
    }

    public synchronized boolean isRented() {
        return rentedBy != null;
    }
}
