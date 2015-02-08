package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarFactory;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent.ReadyTask;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent.RentingTask;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.concurrent.WaitingTask;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rules.JavaFXThreadingRule;

import java.util.concurrent.Phaser;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

public class ClientServiceTest {

    @Rule
    public JavaFXThreadingRule rule = new JavaFXThreadingRule();

    private ViewContext<ClientService> context;
    private ClientService service;

    @Before
    public void setup() throws Exception {
        context = ViewFactory.getInstance().createByController(ClientService.class);
        service = context.getController();
        context.register(new CarFactory("AA", 5));
        service.bind("TestClient", new Phaser(1));
    }

    @After
    public void tearDown() throws Exception {
        context.destroy();
    }

    @Test
    public void testCreateTasks() {
        Task<ClientState> task = service.createTask();
        assertTrue("The given task is a ReadyTask", task instanceof ReadyTask);
        assertNotNull("We received a task", task);
    }

    @Test
    public void testCycling() {
        Task<ClientState> task = service.createTask();
        assertTrue("First task is ReadyTask", task instanceof ReadyTask);
        service.getClient().setClientState(ClientState.WAITING);

        task = service.createTask();
        assertTrue("Second task is WaitingTask", task instanceof WaitingTask);
        service.getClient().setClientState(ClientState.RENTING);

        task = service.createTask();
        assertTrue("Third task is WaitingTask", task instanceof RentingTask);
        service.getClient().setClientState(ClientState.READY);

        task = service.createTask();
        assertTrue("Third task is ReadyTask", task instanceof ReadyTask);
    }
}
