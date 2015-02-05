package no.westerdals.student.vegeiv13.assignment1.carrental;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Client {

    private final ObservableValue<String> name;

    public Client(String name) {
        this.name = new SimpleStringProperty(name);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name=" + name.getValue() +
                '}';
    }

    public String getName() {
        return name.getValue();
    }

    public ObservableValue<String> nameProperty() {
        return name;
    }
}
