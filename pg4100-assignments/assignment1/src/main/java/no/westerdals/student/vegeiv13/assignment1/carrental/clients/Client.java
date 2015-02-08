package no.westerdals.student.vegeiv13.assignment1.carrental.clients;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {

    private final StringProperty name;
    private ClientState state = ClientState.READY;

    /**
     * Sets up a client with a given name
     *
     * @param name the client's name
     */
    public Client(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * Gets this client's state
     *
     * @return this client's state
     */
    public ClientState getClientState() {
        return state;
    }

    /**
     * Sets this client's state
     *
     * @param clientState the new state of the client
     */
    public void setClientState(final ClientState clientState) {
        this.state = clientState;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name=" + name.getValue() +
                '}';
    }

    /**
     * Gets this client's nameProperty object
     * @return this client's nameProperty object
     */
    public StringProperty nameProperty() {
        return name;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Client client = (Client) o;

        return name.get().equals(client.name.get()) && state == client.state;

    }

}
