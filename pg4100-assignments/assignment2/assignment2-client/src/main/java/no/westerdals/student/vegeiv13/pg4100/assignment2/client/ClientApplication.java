package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * Class used to start a JavaFX application
 */
public class ClientApplication extends Application {

    public static void main(String[] args) {
        Application.launch(ClientApplication.class, args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Flow flow = new Flow(HomeController.class);

        FlowHandler flowHandler = flow.createHandler(new ViewFlowContext());
        DefaultFlowContainer container = new DefaultFlowContainer();
        StackPane pane = flowHandler.start(container);
        primaryStage.setScene(new Scene(pane));
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
