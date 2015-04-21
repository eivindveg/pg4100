package no.westerdals.student.vegeiv13.pg4100.assignment3.window;

import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent.NumberInfoTask;
import no.westerdals.student.vegeiv13.pg4100.assignment3.models.NumberInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

@ViewController("Window.fxml")
public class StartController {

    private Logger logger = LoggerFactory.getLogger(StartController.class);

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    @ActionTrigger("close")
    private MenuItem close;

    @FXML
    private TextField input;

    @FXML
    @ActionTrigger("checkNumber")
    private Button checkNumber;

    @FXML
    private ProgressIndicator progressIndicator;

    @PostConstruct
    public void init() {
        progressIndicator.setVisible(false);
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.replaceAll(" ", "").equals("")) {
                return;
            }
            try {
                new BigInteger(newValue);
            } catch (NumberFormatException e) {
                input.setText(oldValue);
            }
        });
    }

    @ActionMethod("checkNumber")
    private void doCheckNumber() {
        String input = this.input.getText();
        if (input.equals("")) {
            logger.warn("Cannot check an empty string value as a number");
            return;
        }
        BigInteger number = new BigInteger(input);
        Task<NumberInfo> numberInfoTask = new NumberInfoTask(number);
        numberInfoTask.setOnSucceeded(event -> {
            if (logger.isDebugEnabled()) {
                logger.debug("NumberInfoTask succeeded");
            }

            NumberInfo result = (NumberInfo) event.getSource().getValue();
            context.register(result);
            try {
                actionHandler.navigate(ResultController.class);
                if (logger.isDebugEnabled()) {
                    logger.debug("Navigated to " + ResultController.class.getSimpleName());
                }
            } catch (VetoException | FlowException e) {
                logger.error("Could not navigate to " + ResultController.class.getSimpleName(), e);
                throw new RuntimeException(e);
            }
        });
        numberInfoTask.setOnScheduled(event -> progressIndicator.setVisible(true));
        if(logger.isDebugEnabled()) {
            logger.debug("Starting NumberInfoTask");
        }
        new Thread(numberInfoTask).start();
    }

    @ActionMethod("close")
    private void doClose() {
        System.exit(0);
    }

}
