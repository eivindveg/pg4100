package no.westerdals.student.vegeiv13.assignment1.carrental.clients;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Client {

    private final ObservableValue<String> name;
    ClientState state = ClientState.READY;

    public Client(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public ClientState getClientState() {
        return state;
    }

    public void setClientState(final ClientState clientState) {
        this.state = clientState;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Client client = (Client) o;

        return name.equals(client.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}