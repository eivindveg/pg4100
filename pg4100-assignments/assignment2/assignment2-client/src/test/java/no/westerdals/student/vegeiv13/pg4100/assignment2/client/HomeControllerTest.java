package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

public class HomeControllerTest extends ApplicationTest {

    @Override
    public void start(final Stage stage) throws Exception {
        Flow flow = new Flow(HomeController.class);
        ViewFlowContext flowContext = new ViewFlowContext();
        FlowHandler flowHandler = flow.createHandler(flowContext);
        DefaultFlowContainer container = new DefaultFlowContainer();
        StackPane pane = flowHandler.start(container);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @Test
    public void testApplicationRunning() {
        verifyThat("#connect", isNotNull());
    }

    @Test
    public void testConnect() throws Exception {
        clickOn("#connect");
        verifyThat("#errorLabel", hasText(Constants.INVALID_USERNAME));

        TextField userInput = lookup("#userInput").queryFirst();
        Platform.runLater(() -> userInput.setText("TestUser"));
        Thread.sleep(1);
        verifyThat("#userInput", hasText("TestUser"));

        TextField hostInput = lookup("#serverInput").queryFirst();
        Platform.runLater(() -> hostInput.setText("localhost"));
        Thread.sleep(1);
        verifyThat("#serverInput", hasText("localhost"));

        clickOn("#connect");
        verifyThat("#errorLabel", hasText(Constants.COULD_NOT_CONNECT));
    }
}
