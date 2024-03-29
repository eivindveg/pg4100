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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ReadyTaskTest {

    @Rule
    public JavaFXThreadingRule rule = new JavaFXThreadingRule();

    private Phaser phaser;
    private Client testClient;
    private CarRental carRental;
    private ReadyTask readyTask;

    @Before
    public void setup() {
        phaser = new Phaser(1);
        testClient = new Client("TestClient");
        carRental = new CarRental("UF", 1);
        readyTask = new ReadyTask(testClient, carRental, phaser, 0);
    }

    @Test
    public void testTaskReturnsWaiting() throws Exception {
        Thread thread = new Thread(readyTask);
        thread.start();
        assertTrue("Task finished and returned waiting", readyTask.get().equals(ClientState.WAITING));
        carRental.returnCarByClient(testClient);
    }

    @Test
    public void testTaskWaitsDuration() throws Exception {
        ReadyTask waitingTask = new ReadyTask(testClient, carRental, phaser, 10);
        Thread thread = new Thread(waitingTask);
        thread.start();
        Thread.sleep(1);
        assertFalse("Thread is not yet done", waitingTask.isDone());
        Thread.sleep(10);
        assertNotNull("Thread is done", waitingTask.get());
    }
}
