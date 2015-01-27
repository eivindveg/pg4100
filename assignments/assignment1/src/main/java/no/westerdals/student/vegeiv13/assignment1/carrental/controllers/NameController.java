package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.util.VetoException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@FXMLController(value = "/names.fxml", title = "CarRental - Input client names")
public class NameController extends BorderPane {

    @FXML
    @ActionTrigger("doneAction")
    private Button done;

    @FXML
    @ActionTrigger("quitAction")
    private Button quit;

    @FXML
    private VBox listView;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @ActionHandler
    private FlowActionHandler actionHandler;

    private EventHandler<KeyEvent> handler;

    private void addNewItem() {
        TextField e = new TextField();
        e.setOnKeyPressed(handler);
        listView.getChildren().add(e);
        e.requestFocus();
    }

    @ActionMethod("quitAction")
    public void onQuit() {
        System.exit(0);
    }

    @ActionMethod("doneAction")
    public void onDone() throws VetoException, FlowException {
        context.register("names", getNames());
        actionHandler.navigate(MainController.class);
    }

    private List<String> getNames() {
        return listView.getChildren()
                .stream()
                .filter(e -> !((TextField) e)
                        .getText()
                        .equals(""))
                .map(e -> ((TextField) e).getText())
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void initiate() {
        handler = event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addNewItem();
            }
        };
        addNewItem();
    }
}
