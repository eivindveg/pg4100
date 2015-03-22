package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.datafx.controller.context.AbstractContext;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;

public class HomeControllerTest extends ApplicationTest {

    private Bootstrap bootstrap;
    private ChannelFuture channel;
    private ViewFlowContext flowContext;
    private FlowHandler flowHandler;

    @SuppressWarnings("unchecked")
    @Override
    public void start(final Stage stage) throws Exception {
        Flow flow = new Flow(HomeController.class);
        flowContext = new ViewFlowContext();

        bootstrap = mock(Bootstrap.class);
        channel = mock(ChannelFuture.class);
        when(bootstrap.channel(any())).thenReturn(bootstrap);
        when(bootstrap.connect("localhost", Constants.PORT)).thenReturn(channel);
        when(bootstrap.connect("", Constants.PORT)).thenThrow(ConnectException.class);
        when(channel.syncUninterruptibly()).thenReturn(channel);

        flowContext.register(bootstrap, Bootstrap.class);

        flowHandler = flow.createHandler(flowContext);
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
        TextField hostInput = lookup("#serverInput").queryFirst();
        TextField userInput = lookup("#userInput").queryFirst();

        clickOn("#connect");
        verifyThat("#errorLabel", hasText(Constants.INVALID_USERNAME));

        Platform.runLater(() -> userInput.setText("TestUser"));
        Thread.sleep(1);
        verifyThat("#userInput", hasText("TestUser"));

        clickOn("#connect");
        verifyThat("#errorLabel", hasText(Constants.COULD_NOT_CONNECT));

        Platform.runLater(() -> hostInput.setText("localhost"));
        Thread.sleep(1);
        verifyThat("#serverInput", hasText("localhost"));

        clickOn("#connect");
    }

    @Test
    public void testControllerCanSetupBootstrap() throws Exception {
        hackRegistry();
        HomeController controller = (HomeController) flowHandler.getCurrentViewContext().getController();
        controller.init();
        Method getBootstrap = controller.getClass().getDeclaredMethod("getBootstrap");
        getBootstrap.setAccessible(true);

        Bootstrap invoke = (Bootstrap) getBootstrap.invoke(controller);
        invoke.validate();
    }

    /**
     * Ugly hack, but it's required to get around ambigious calls for overloaded methods when using null values
     */
    @SuppressWarnings("unchecked")
    private void hackRegistry() throws Exception {
        Field registeredObjects = AbstractContext.class.getDeclaredField("registeredObjects");
        registeredObjects.setAccessible(true);
        Map<String, Object> registry = (Map<String, Object>) registeredObjects.get(flowContext);
        registry.remove(Bootstrap.class.toString());
    }
}
