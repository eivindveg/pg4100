package no.westerdals.student.vegeiv13.pg4100.assignment3;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment3.window.ResultController;
import no.westerdals.student.vegeiv13.pg4100.assignment3.window.StartController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eivind Vegsundvåg
 * @since 1.0
 */
public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        javafx.application.Application.launch(Application.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Starts the JavaFX application
     *
     * @since 1.0
     * @param primaryStage The JavaFX window stage to render in
     * @throws FlowException if the application could not load
     */
    @Override
    public void start(final Stage primaryStage) throws FlowException {
        if(logger.isDebugEnabled()){
            logger.info("Starting application");
        }
        Flow flow = new Flow(StartController.class)
                .withGlobalLink("checkNumber", ResultController.class)
                .withGlobalAction("goBack", (flowHandler, actionId) -> {
                    try {
                        flowHandler.navigateTo(StartController.class);
                    } catch (VetoException e) {
                        throw new FlowException(e);
                    }
                });
        ViewFlowContext context = new ViewFlowContext();
        FlowHandler handler = flow.createHandler(context);
        StackPane start;
        try {
            start = handler.start();
        } catch (FlowException e) {
            logger.error("Could not instantiate application", e);
            throw e;
        }
        primaryStage.setScene(new Scene(start));
        primaryStage.show();
        if(logger.isDebugEnabled()) {
            logger.info("Started application");
        }
    }
}
