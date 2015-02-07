package no.westerdals.student.vegeiv13.assignment1.carrental.clients;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

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
        assert !client.equals(new Object());
        assert client.equals(new Client("TestClient"));
        assert !client.equals(new Client("AnotherClient"));
    }
}
