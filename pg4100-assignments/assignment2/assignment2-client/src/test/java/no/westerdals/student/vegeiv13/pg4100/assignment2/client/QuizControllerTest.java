package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuizControllerTest extends ApplicationTest {

    Player player = new Player("TestUser");

    @Override
    public void start(final Stage stage) throws Exception {
        Flow flow = new Flow(QuizController.class);
        ViewFlowContext flowContext = new ViewFlowContext();

        Channel channel = mock(NioSocketChannel.class);
        when(channel.pipeline()).thenReturn(mock(ChannelPipeline.class));
        when(channel.writeAndFlush(player)).thenReturn(mock(ChannelFuture.class));
        flowContext.register(NioSocketChannel.class.toString(), channel);
        flowContext.register(player);

        FlowHandler flowHandler = flow.createHandler(flowContext);
        DefaultFlowContainer container = new DefaultFlowContainer();
        StackPane pane = flowHandler.start(container);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @Test
    public void test() {

    }
}
