package no.westerdals.student.vegeiv13.assignment1.carrental.clients;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    private static final String CLIENT_NAME = "TestClient";
    private Client client;

    @Before
    public void setup() {
        client = new Client(CLIENT_NAME);
    }

    @Test
    public void testClientNameFromProperty() {
        String expected = CLIENT_NAME;
        String actual = client.nameProperty().getValue();

        assertSame("Names match", expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "Client{name=TestClient}";
        String actual = client.toString();

        assert expected.equals(actual);
    }

    @Test
    public void testEquals() {
        assertFalse("Client does not equal dummy object", client.equals(new Object()));
        assertTrue("Client equals a client with the same name", client.equals(new Client("TestClient")));
        assertFalse("Client does not equal a client with another name", client.equals(new Client("AnotherClient")));
    }

    @Test
    public void testSetClientState() {
        client.setClientState(ClientState.RENTING);
        assertTrue("Client state changed", client.getClientState().equals(ClientState.RENTING));
    }
}
