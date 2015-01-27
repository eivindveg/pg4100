package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class NameController extends BorderPane {

    @FXML
    public Button done = new Button();
    @FXML
    public Button quit = new Button();
    @FXML
    private VBox listView = new VBox();

    private List<ChangeListener<? super List<String>>> changeListeners = new ArrayList<>();

    private EventHandler<KeyEvent> handler = event -> {
        if (event.getCode() == KeyCode.ENTER) {
            addNewItem();
        }
    };

    public NameController() {
        // TODO RIG DONE BUTTON
        quit.setOnAction(event -> System.exit(0));
    }

    private void addNewItem() {
        this.centerProperty().setValue(listView);
        TextField e = new TextField();
        e.setOnKeyPressed(handler);
        listView.getChildren().add(e);
        e.requestFocus();
    }

    public void initiate() {
        addNewItem();
    }
}
