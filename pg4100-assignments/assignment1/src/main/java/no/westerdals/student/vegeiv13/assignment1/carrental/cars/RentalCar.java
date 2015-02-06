package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import com.sun.istack.internal.Nullable;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;

public class RentalCar {

    private final String registrationNumber;
    private Client rentedBy;

    protected RentalCar(final String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public synchronized
    @Nullable
    Client getRentedBy() {
        return rentedBy;
    }

    public synchronized void setRentedBy(final @Nullable Client rentedBy) {
        this.rentedBy = rentedBy;
    }

    @Override
    public String toString() {
        return "RentalCar{" +
                "rentedBy=" + rentedBy +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    public synchronized boolean isRented() {
        return rentedBy != null;
    }
}
