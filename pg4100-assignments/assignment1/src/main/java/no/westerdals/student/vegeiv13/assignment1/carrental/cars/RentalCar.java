package no.westerdals.student.vegeiv13.assignment1.carrental.cars;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;

public class RentalCar {

    private final StringProperty registrationNumber;
    private Property<Client> rentedBy = new SimpleObjectProperty<>();

    protected RentalCar(final String registrationNumber) {
        this.registrationNumber = new SimpleStringProperty(registrationNumber);
    }

    public String getRegistrationNumber() {
        return registrationNumber.get();
    }

    public StringProperty registrationNumberProperty() {
        return registrationNumber;
    }

    public synchronized Client getRentedBy() {
        return rentedBy.getValue();
    }

    public synchronized void setRentedBy(final Client rentedBy) {
        this.rentedBy.setValue(rentedBy);
    }

    public Property<Client> clientProperty() {
        return rentedBy;
    }

    @Override
    public String toString() {
        return "RentalCar{" +
                "rentedBy=" + rentedBy +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    public synchronized boolean isRented() {
        return rentedBy.getValue() != null;
    }
}
