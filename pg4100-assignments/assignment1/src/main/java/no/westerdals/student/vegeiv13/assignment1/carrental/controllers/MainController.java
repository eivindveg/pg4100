package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.CarRental;
import no.westerdals.student.vegeiv13.assignment1.carrental.cars.RentalCar;
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
import java.util.concurrent.Phaser;

@FXMLController("/window.fxml")
public class MainController {

    public static final int REQUIRED_PARTIES = 5;

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

    @FXML
    @ActionTrigger("addCar")
    private Button addCar;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    private CarRental carRental;

    private Phaser phaser;

    @PostConstruct
    public void init() {
        phaser = new Phaser(REQUIRED_PARTIES);
        carRental = new CarRental("UF");
        carRental.getRentalCarsUnmodifiable().forEach(car -> {
                    try {
                        setupCar(car);
                    } catch (FxmlLoadException e) {
                        triggerFatalError();
                    }
                }
        );
        nameInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addClient();
            }
        });

    }

    private void setupCar(final RentalCar car) throws FxmlLoadException {
        ViewContext<CarController> viewContext = ViewFactory.getInstance().createByController(CarController.class);
        CarController controller = viewContext.getController();
        controller.bind(car);
        Node root = viewContext.getRootNode();
        carBox.getChildren().add(root);
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
    public void addClient() {
        String name = nameInput.getText();
        if (name == null || name.replace(" ", "").equals("")) {
            return;
        }
        nameInput.clear();
        try {
            ViewContext<ClientService> context = ViewFactory.getInstance().createByController(ClientService.class);
            context.register(carRental);
            ClientService controller = context.getController();
            controller.bind(name, phaser);
            controller.setOnSucceeded(e -> transition(controller.getValue(), context));
            readyBox.getChildren().addAll(context.getRootNode());
            controller.start();
        } catch (FxmlLoadException e) {
            triggerFatalError();
        }
    }

    private void moveAdapter(Node node, VBox moveFrom, VBox moveTo) {
        moveFrom.getChildren().remove(node);
        moveTo.getChildren().add(node);
    }

    @ActionMethod("addCar")
    public void addCar() {
        try {
            setupCar(carRental.addNewCar());
        } catch (FxmlLoadException e) {
            triggerFatalError();
        }
    }

    private void triggerFatalError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "We have encountered a fatal error accessing local resources" +
                "and will now exit", ButtonType.CLOSE);
        alert.show();
        alert.setOnCloseRequest(event -> Platform.exit());
    }
}
