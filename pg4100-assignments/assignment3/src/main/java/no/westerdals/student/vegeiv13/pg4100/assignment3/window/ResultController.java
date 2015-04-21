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
import javafx.scene.control.Label;
import no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent.NumberInfoTask;
import no.westerdals.student.vegeiv13.pg4100.assignment3.models.NumberInfo;

import javax.annotation.PostConstruct;

@ViewController("Results.fxml")
public class ResultController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXML
    private Label value;

    @FXML
    private Label isPrime;

    @FXML
    private Label isOdd;

    @FXML
    @ActionTrigger("checkNextPrime")
    private Button nextPrime;

    @FXML
    @ActionTrigger("goBack")
    private Button goBack;
    private NumberInfo registeredObject;

    @PostConstruct
    public void init() {
        registeredObject = context.getRegisteredObject(NumberInfo.class);
        value.textProperty().bind(registeredObject.stringValueProperty());
        isPrime.textProperty().bind(registeredObject.stringPrimeProperty());
        isOdd.textProperty().bind(registeredObject.stringOddProperty());
        nextPrime.textProperty().bind(registeredObject.stringNextPrimeProperty());
    }

    @ActionMethod("checkNextPrime")
    private void doCheckNextPrime() {
        Task<NumberInfo> numberInfoTask = new NumberInfoTask(registeredObject.getNextPrime());
        numberInfoTask.setOnSucceeded(event -> {
            NumberInfo result = (NumberInfo) event.getSource().getValue();
            context.register(result);
            try {
                actionHandler.navigate(ResultController.class);
            } catch (VetoException | FlowException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(numberInfoTask).start();
    }

}
