package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import no.westerdals.student.vegeiv13.assignment1.carrental.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.clients.ClientState;
import org.datafx.controller.FXMLController;
import org.datafx.controller.FxmlLoadException;
import org.datafx.controller.ViewFactory;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.annotation.PostConstruct;

@FXMLController("/window.fxml")
public class MainController {

    @FXML
    private VBox readyBox;

    @FXML
    private VBox waitingBox;

    @FXML
    private VBox rentingBox;

    @FXML
    private VBox carBox;

    @FXML
    private TextField nameInput;

    @FXML
    @ActionTrigger("addClient")
    private Button addClient;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    private CarRental carRental;

    @PostConstruct
    public void init() {
        carRental = new CarRental("UF");
        carRental.getRentalCarsUnmodifiable().forEach( car -> {
                    try {
                        ViewContext<CarController> viewContext = ViewFactory.getInstance().createByController(CarController.class);
                        CarController controller = viewContext.getController();
                        controller.bind(car);
                        Node root = viewContext.getRootNode();
                        carBox.getChildren().add(root);
                    } catch (FxmlLoadException e) {
                        e.printStackTrace();
                    }
                }
        );
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onAddClient();
            }
        });

    }

    private void transition(ClientState state, ViewContext context) {
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
    }

    @ActionMethod("addClient")
    public void onAddClient() {
        String name = nameInput.getText();
        if (name == null || name.replace(" ", "").equals("")) {
            return;
        }
        nameInput.clear();
        try {
            ViewContext<ClientService> context = ViewFactory.getInstance().createByController(ClientService.class);
            ClientService controller = context.getController();
            controller.bind(name);
            context.register(carRental);
            controller.setOnSucceeded(e -> transition(controller.getValue(), context));
            readyBox.getChildren().addAll(context.getRootNode());
            controller.start();
        } catch (FxmlLoadException e) {
            e.printStackTrace();
        }
    }

    private void moveAdapter(Node node, VBox moveFrom, VBox moveTo) {
        moveFrom.getChildren().remove(node);
        moveTo.getChildren().add(node);
    }
}
