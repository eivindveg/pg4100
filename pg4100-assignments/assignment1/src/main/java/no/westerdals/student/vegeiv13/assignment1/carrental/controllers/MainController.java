package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;
import org.datafx.controller.FXMLController;
import org.datafx.controller.FxmlLoadException;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@FXMLController("/window.fxml")
public class MainController implements ClientAdapterNotifier {

    @FXML
    private VBox readyBox;

    @FXML
    private VBox waitingBox;

    @FXML
    private VBox rentingBox;

    private Executor executor;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        List<String> names = (List<String>) context.getRegisteredObject("names");
        executor = Executors.newFixedThreadPool(names.size());
        System.out.println(names);
        names.forEach(name -> {
            try {
                ViewContext<ClientService> context = ViewFactory.getInstance().createByController(ClientService.class);
                ClientService controller = context.getController();
                System.out.println(controller);
                controller.bind(name);
                startThread(controller);
                controller.setOnSucceeded(e -> transition(controller, context));
                readyBox.getChildren().addAll(context.getRootNode());
            } catch (FxmlLoadException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void transition(ClientService adapter, ViewContext context) {
        ClientState state = adapter.getClientState();
        VBox moveFrom = null;
        VBox moveTo = null;

        switch (state) {
            case READY:
                moveFrom = rentingBox;
                moveTo = readyBox;
                break;
            case WAITING:
                moveFrom = readyBox;
                moveTo = waitingBox;
                break;
            case RENTING:
                moveFrom = waitingBox;
                moveTo = rentingBox;
        }

        moveAdapter(context.getRootNode(), moveFrom, moveTo);
        startThread(adapter);
    }

    private void startThread(final ClientService adapter) {
        adapter.restart();
        executor.execute(adapter.createTask());
    }

    private void moveAdapter(Node node, VBox moveFrom, VBox moveTo) {
        moveFrom.getChildren().remove(node);
        if(!moveTo.getChildren().contains(node)) {
            moveTo.getChildren().add(node);
        }
    }
}
