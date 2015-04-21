package no.westerdals.student.vegeiv13.pg4100.assignment3.window;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;

public class ResultControllerTest extends ApplicationTest {

    private StackPane root;
    private ResultController resultController;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Flow flow = new Flow(ResultController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        FlowHandler handler = flow.createHandler(new ViewFlowContext());
        root = handler.start(container);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        resultController = (ResultController) handler.getCurrentViewContext().getController();
    }
}
