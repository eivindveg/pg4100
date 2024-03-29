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
 * @author Eivind Vegsundvaag
 * @since 1.0
 */
public class Application extends javafx.application.Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        javafx.application.Application.launch(Application.class, args);
    }

    /**
     * Starts the JavaFX application
     *
     * @param primaryStage The JavaFX window stage to render in
     * @since 1.0
     */
    @Override
    public void start(final Stage primaryStage) {
        logger.info("Starting application");
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
        try {
            final StackPane start = handler.start();
            primaryStage.setScene(new Scene(start));
            primaryStage.show();
            logger.info("Started application");
        } catch (FlowException e) {
            logger.error("Could not instantiate application", e);
        }
    }
}
