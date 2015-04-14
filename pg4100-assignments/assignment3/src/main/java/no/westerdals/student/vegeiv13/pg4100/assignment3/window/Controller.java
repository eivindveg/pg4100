package no.westerdals.student.vegeiv13.pg4100.assignment3.window;

import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

@ViewController("Window.fxml")
public class Controller {

    private Logger logger = LoggerFactory.getLogger(Controller.class);

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXML
    @ActionTrigger("close")
    private MenuItem close;

    @FXML
    private TextField input;

    @PostConstruct
    public void init() {
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.replaceAll(" ", "").equals("")) {
                return;
            }
            try {
                new BigInteger(newValue);
            } catch(NumberFormatException e) {
                input.setText(oldValue);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    @ActionMethod("close")
    private void doClose() {
        System.exit(0);
    }

}
