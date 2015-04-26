package no.westerdals.student.vegeiv13.pg4100.assignment3.models;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * Value object for information about a {@link BigInteger}
 *
 * @author Eivind Vegsundvaag
 * @since 1.0
 */
public class NumberInfo {

    private final ObjectProperty<BigInteger> value = new SimpleObjectProperty<>();
    private final BooleanProperty isPrime = new SimpleBooleanProperty();
    private final ObjectProperty<BigInteger> nextPrime = new SimpleObjectProperty<>();
    private final BooleanProperty isOdd = new SimpleBooleanProperty();


    public NumberInfo(final @NotNull BigInteger value, final boolean isPrime,
                      final @NotNull BigInteger nextPrime, final boolean isOdd) {
        this.value.set(value);
        this.isPrime.set(isPrime);
        this.nextPrime.set(nextPrime);
        this.isOdd.set(isOdd);
    }

    /**
     * Gets the value of the {@link BigInteger} this object represents
     *
     * @return The {@link BigInteger} held by this object's value property
     */
    public BigInteger getValue() {
        return value.get();
    }

    /**
     * Returns a self updating string property wrapper for the BigInteger value this object holds. Simplifies binding
     * to text fields
     *
     * @return A StringProperty that mirrors {@link #valueProperty()}
     */
    public StringProperty stringValueProperty() {
        return toStringProperty(value);
    }

    /**
     * Gets the next {@link BigInteger} that is a prime
     * @return the next prime
     */
    public BigInteger getNextPrime() {
        return nextPrime.get();
    }

    /**
     * Whether or not this is an odd number, described as not being divisible by 2.
     * @return whether or not this is an odd number
     */
    public boolean isOdd() {
        return isOdd.get();
    }

    /**
     *
     * @return
     */
    public boolean isPrime() {
        return isPrime.get();
    }

    public ReadOnlyBooleanProperty isPrimeProperty() {
        return SimpleBooleanProperty.readOnlyBooleanProperty(isPrime);
    }

    public StringProperty stringPrimeProperty() {
        return toStringProperty(isPrime);
    }

    @SuppressWarnings("unchecked")
    private StringProperty toStringProperty(Property property) {
        final StringProperty stringProperty = new SimpleStringProperty(property.getValue().toString());
        property.addListener((observable, oldValue, newValue) -> stringProperty.setValue(newValue.toString()));

        ReadOnlyStringWrapper readOnlyStringWrapper = new ReadOnlyStringWrapper();
        readOnlyStringWrapper.bind(stringProperty);
        return readOnlyStringWrapper;
    }

    public ReadOnlyObjectWrapper<BigInteger> nextPrimeProperty() {
        ReadOnlyObjectWrapper<BigInteger> objectReadOnlyObjectWrapper = new ReadOnlyObjectWrapper<>();
        objectReadOnlyObjectWrapper.bind(nextPrime);
        return objectReadOnlyObjectWrapper;
    }

    public StringProperty stringNextPrimeProperty() {
        return toStringProperty(nextPrime);
    }

    public ObservableValue<? extends BigInteger> valueProperty() {
        ReadOnlyObjectWrapper<BigInteger> readOnlyObjectWrapper = new ReadOnlyObjectWrapper<>();
        readOnlyObjectWrapper.bind(value);
        return readOnlyObjectWrapper;
    }

    public ObservableValue<? extends Boolean> isOddProperty() {
        return SimpleBooleanProperty.readOnlyBooleanProperty(isOdd);
    }

    public StringProperty stringOddProperty() {
        return toStringProperty(isOdd);
    }
}
