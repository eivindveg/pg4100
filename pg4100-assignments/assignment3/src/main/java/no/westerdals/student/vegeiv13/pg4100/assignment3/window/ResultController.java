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

/**
 * Controller class responsible for managing a view stage as specified in this class' {@link ViewController}
 *
 * Namely, this controller is dependent on having a {@link NumberInfo} object in its {@link ViewFlowContext} to display
 * information about it.
 *
 * @author Eivind Vegsundvåg
 * @since 1.0
 */
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

    /**
     * PostConstructor called by DataFX
     *
     * Necessary as {@link FXML}-annotated objects are injected after the initial constructor call.
     *
     * @since 1.0
     */
    @PostConstruct
    public void init() {
        updateFromContext();
    }

    /**
     * Triggered by {@link #init()} when this controller starts. This method is responsible for binding the
     * {@link NumberInfo}'s values to the view by getting the registered {@link NumberInfo} from the DataFX
     * {@link ViewFlowContext}
     *
     * @since 1.0
     */
    private void updateFromContext() {
        registeredObject = context.getRegisteredObject(NumberInfo.class);
        if(registeredObject == null) {
            return;
        }
        value.textProperty().bind(registeredObject.stringValueProperty());
        isPrime.textProperty().bind(registeredObject.stringPrimeProperty());
        isOdd.textProperty().bind(registeredObject.stringOddProperty());
        nextPrime.textProperty().bind(registeredObject.stringNextPrimeProperty());
    }

    /**
     * Triggered by clicking the {@link #nextPrime} button. This starts a new {@link NumberInfoTask} and re-navigates
     * to this controller so that {@link #updateFromContext} rebinds to the new object.
     *
     * @since 1.0
     */
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
