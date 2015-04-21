package no.westerdals.student.vegeiv13.pg4100.assignment3.window;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent.NumberInfoTask;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.junit.Assert.*;

public class ResultControllerTest extends ApplicationTest {

    private StackPane root;
    private ResultController resultController;
    private ViewFlowContext flowContext;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Flow flow = new Flow(ResultController.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        flowContext = new ViewFlowContext();
        FlowHandler handler = flow.createHandler(flowContext);
        root = handler.start(container);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        resultController = (ResultController) handler.getCurrentViewContext().getController();
    }

    @Test
    public void testViewDisplaysValues() throws Exception {
        updateNumberInfo("2");

        verifyThat("#value", hasText("2"));
        verifyThat("#isOdd", hasText("false"));
        verifyThat("#isPrime", hasText("true"));
        verifyThat("#nextPrime", hasText("3"));
    }

    @Test
    public void testNextPrimeFlowsView() throws Exception {
        updateNumberInfo("2");

        clickOn("#nextPrime");
        verifyThat("#value", hasText("3"));
        verifyThat("#isOdd", hasText("true"));
        verifyThat("#isPrime", hasText("true"));
        verifyThat("#nextPrime", hasText("5"));
    }

    private void updateNumberInfo(final String numberString) throws Exception {
        NumberInfoTask task = new NumberInfoTask(new BigInteger(numberString));
        new Thread(task).start();
        flowContext.register(task.get(3, TimeUnit.SECONDS));
        Method method = ResultController.class.getDeclaredMethod("updateFromContext");
        method.setAccessible(true);
        final List<Exception> exceptionList = new ArrayList<>();
        Platform.runLater(() -> {
            try {
                method.invoke(resultController);
            } catch (IllegalAccessException | InvocationTargetException e) {
                exceptionList.add(e);
            }
        });
        Thread.sleep(1000);
        assertTrue("No exceptions caught during Platform.invokeLater invocation", exceptionList.isEmpty());
    }
}
