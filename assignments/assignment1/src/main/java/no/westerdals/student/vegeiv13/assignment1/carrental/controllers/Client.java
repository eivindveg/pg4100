package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Client extends VBox implements Runnable {

    private String name;

    @FXML
    private ProgressBar progress;

    @FXML
    private Label label;

    public Client() {}

    public Client(final String name) {
        this.name = name;
    }

    @Override
    public void run() {
        throw new NotImplementedException();
    }

    public String getName() {
        return name;
    }
}
