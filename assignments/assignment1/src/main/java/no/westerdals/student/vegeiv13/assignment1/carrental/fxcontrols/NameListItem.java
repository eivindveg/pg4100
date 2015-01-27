package no.westerdals.student.vegeiv13.assignment1.carrental.fxcontrols;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class NameListItem extends HBox {

    private TextField name;

    public NameListItem(EventHandler<KeyEvent> handler) {
        name = new TextField();
        name.setOnKeyPressed(handler);
        this.getChildren().add(name);
    }

    public String getName() {
        return name.getText();
    }
}
