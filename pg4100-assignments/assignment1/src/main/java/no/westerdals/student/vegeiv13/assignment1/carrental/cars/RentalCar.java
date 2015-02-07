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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RentalCar rentalCar = (RentalCar) o;

        return registrationNumber.equals(rentalCar.registrationNumber)
                && !(rentedBy != null ? !rentedBy.equals(rentalCar.rentedBy) : rentalCar.rentedBy != null);

    }

    @Override
    public int hashCode() {
        int result = registrationNumber.hashCode();
        result = 31 * result + (rentedBy != null ? rentedBy.hashCode() : 0);
        return result;
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
