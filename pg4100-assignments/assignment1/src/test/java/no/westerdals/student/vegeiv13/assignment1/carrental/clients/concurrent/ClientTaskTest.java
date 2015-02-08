package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Phaser;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ClientTaskTest {

    private ClientTask task;
    private Phaser phaser;
    private Client testClient;
    private CarRental carRental;

    @Before
    public void setup() throws Exception {
        phaser = new Phaser(1);
        testClient = new Client("TestClient");
        carRental = new CarRental("UF", 1);
        task = new ClientTask(testClient, carRental, phaser) {
            @Override
            protected ClientState call() throws Exception {
                return ClientState.READY;
            }
        };
    }

    @Test
    public void testRunRegistersOnPhaser() {
        assertSame("Phaser has no arrived parties", 0, phaser.getArrivedParties());
        task.run();
        assertSame("Phaser has is not missing any parties", 0, phaser.getUnarrivedParties());
    }

    @Test
    public void testGetClient() {
        Client client = task.getClient();
        assertSame("The client object is the same as we passed it", testClient, client);
    }

    @Test
    public void testGetCarRental() {
        CarRental rental = task.getCarRental();
        assertSame("The car rental object is the same as we passed it", carRental, rental);
    }

    @Test
    public void testClientTaskPausesOnPhaser() throws Exception {
        Phaser blockingPhaser = new Phaser(2);
        ClientTask taskThatWaits = new ClientTask(testClient, carRental, blockingPhaser) {
            @Override
            protected ClientState call() throws Exception {
                return ClientState.READY;
            }
        };
        Thread thread = new Thread(taskThatWaits);
        thread.start();
        Thread.sleep(5);
        assertTrue("Phaser was reduced by only one", blockingPhaser.getArrivedParties() == 1);
        thread.interrupt();
        Thread.sleep(5);
        assertNull("Task did not complete", taskThatWaits.getValue());
        assertFalse("Phaser is not terminated", blockingPhaser.isTerminated());
    }
}
