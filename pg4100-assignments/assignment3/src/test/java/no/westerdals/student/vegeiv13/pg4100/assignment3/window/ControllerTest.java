package no.westerdals.student.vegeiv13.pg4100.assignment3.window;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.testfx.framework.junit.ApplicationTest;

import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

@NotThreadSafe
public class ControllerTest extends ApplicationTest {

    @Rule
    public final ExpectedSystemExit expectedSystemExit = ExpectedSystemExit.none();

    private Controller controller;
    private StackPane root;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Flow flow = new Flow(Controller.class);
        DefaultFlowContainer container = new DefaultFlowContainer();
        FlowHandler handler = flow.createHandler(new ViewFlowContext());
        root = handler.start(container);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        controller = (Controller) handler.getCurrentViewContext().getController();
    }

    @Test
    public void testApplicationRunning() {
        assertTrue("Application is running and visible", root.isVisible());
    }

    @Test(expected = InvocationTargetException.class)
    public void testClose() throws Exception {
        expectedSystemExit.expectSystemExitWithStatus(0);

        /* We're not clicking as this puts the expected exception into an asynchronous thread stack
           this means we'd have to add code to the controller especially to squelch a test exception
        clickOn("#fileMenu");
        clickOn("#close");
        */
        Method doClose = Controller.class.getDeclaredMethod("doClose");
        doClose.setAccessible(true);
        doClose.invoke(controller);
    }

    @Test
    public void testOpenFileMenu() throws InterruptedException {
        MenuBar menuBar = lookup("#menu").queryFirst();

        menuBar.getMenus().stream()
                // If the node doesn't have an ID, the controller cannot override its properties, so it would have no
                // special behaviour
                .filter(node -> node.getId() != null && !node.getId().equals(""))
                .forEach(menu -> {
                    String selector = "#" + menu.getId();
                    clickOn(selector);
                    assertTrue("Menu " + menu.getId() + " is showing after first click", menu.isShowing());
                    clickOn(selector);
                    assertFalse("Menu " + menu.getId() + " is not showing after second click", menu.isShowing());
                });

    }

    @Test
    public void testInputDoesNotAcceptLetters() {
        clickOn("#input");
        type(KeyCode.K, 4);
        type(KeyCode.getKeyCode("8"));
        type(KeyCode.L, 8);

        TextField input = lookup("#input").queryFirst();

        String expected = "8";
        String actual = input.getText();

        assertEquals("Input did not accept letters", expected, actual);

        type(KeyCode.BACK_SPACE, actual.length());
    }

}
