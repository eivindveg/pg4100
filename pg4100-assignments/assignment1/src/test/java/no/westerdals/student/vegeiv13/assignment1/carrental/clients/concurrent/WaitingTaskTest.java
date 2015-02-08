package no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent;

import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.Client;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rules.JavaFXThreadingRule;

import java.util.concurrent.Phaser;

import static junit.framework.Assert.assertTrue;

public class WaitingTaskTest {

    @Rule
    public JavaFXThreadingRule rule = new JavaFXThreadingRule();

    private Phaser phaser;
    private Client testClient;
    private CarRental carRental;
    private WaitingTask waitingTask;

    @Before
    public void setup() {
        phaser = new Phaser(1);
        testClient = new Client("TestClient");
        carRental = new CarRental("UF", 1);
        waitingTask = new WaitingTask(testClient, carRental, phaser);
    }

    @Test
    public void testTaskReturnsWaiting() throws Exception {
        Thread thread = new Thread(waitingTask);
        thread.start();
        assertTrue("Task finished and returned waiting", waitingTask.get().equals(ClientState.RENTING));
        carRental.returnCarByClient(testClient);
    }
}
