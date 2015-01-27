package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@FXMLController("/window.fxml")
public class MainController implements ClientAdapterNotifier {

    @FXML
    public VBox readyBox;

    @FXML
    public VBox waitingBox;

    @FXML
    public VBox rentingBox;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    private List<ClientAdapter> clientAdapters = new ArrayList<>();

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        List<String> names = (List<String>) context.getRegisteredObject("names");
        names.forEach(n -> {
            ClientAdapter controller = new ClientAdapter();
            clientAdapters.add(controller);
            controller.bind(n, this);
            new Thread(controller).start();

        });
        readyBox.getChildren().addAll(clientAdapters);
    }

    @Override
    public void transition(ClientAdapter adapter, ClientState state) {
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

        moveAdapter(adapter, moveFrom, moveTo);

    }

    private void moveAdapter(Node node, VBox moveFrom, VBox moveTo) {
        moveFrom.getChildren().remove(node);
        moveTo.getChildren().add(node);
    }
}
