package no.westerdals.student.vegeiv13.pg4100.assignment3.models;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

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
     * Gets the value of the biginteger this object represents
     * @return
     */
    public BigInteger getValue() {
        return value.get();
    }

    /**
     * Returns a self updating string property wrapper for the BigInteger value this object holds. Simplifies binding
     * to text fields
     * @return A StringProperty that mirrors {@link NumberInfo#valueProperty()}
     */
    public StringProperty stringValueProperty() {
        return toStringProperty(value);
    }

    public BigInteger getNextPrime() {
        return nextPrime.get();
    }

    public boolean isOdd() {
        return isOdd.get();
    }

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
