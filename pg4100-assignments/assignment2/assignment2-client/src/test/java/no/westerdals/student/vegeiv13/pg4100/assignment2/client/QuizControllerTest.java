package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

public class QuizControllerTest extends ApplicationTest {

    public static final String WHAT_IS_THIS = "What is this?";
    Player player;
    private NioSocketChannel channel;
    private FlowHandler flowHandler;
    private QuizController controller;
    private Quiz quiz;

    @Override
    public void start(final Stage stage) throws Exception {
        player = new Player("TestUser");
        player.setScore(10);
        Flow flow = new Flow(QuizController.class);
        ViewFlowContext flowContext = new ViewFlowContext();

        channel = mock(NioSocketChannel.class);
        trainChannelMock();

        flowContext.register(channel, NioSocketChannel.class);
        flowContext.register(player);

        flowHandler = flow.createHandler(flowContext);
        DefaultFlowContainer container = new DefaultFlowContainer();
        StackPane pane = flowHandler.start(container);
        stage.setScene(new Scene(pane));
        stage.show();
        controller = (QuizController) flowHandler.getCurrentViewContext().getController();
    }

    private void trainChannelMock() {
        when(channel.isActive()).thenReturn(true);
        when(channel.isOpen()).thenReturn(true);
        when(channel.pipeline()).thenReturn(mock(ChannelPipeline.class));
        when(channel.writeAndFlush(player)).thenReturn(mock(ChannelFuture.class));
        when(channel.writeAndFlush(any(Quiz.class))).thenReturn(mock(ChannelFuture.class));
    }

    @Before
    public void setup() {
        quiz = new Quiz(WHAT_IS_THIS, null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResetTimer() throws Exception {

        Method resetTimer = QuizController.class.getDeclaredMethod("resetTimer");
        resetTimer.setAccessible(true);

        Field task = QuizController.class.getDeclaredField("task");
        task.setAccessible(true);
        Object nullTask = task.get(controller);
        assertNull("Task has not started", nullTask);


        Platform.runLater(() -> {
            try {
                resetTimer.invoke(controller);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Test failure");
            }
        });
        Thread.sleep(20);
        Task<Void> voidTask = (Task<Void>) task.get(controller);
        assertFalse("Task is running", voidTask.isCancelled());
        voidTask.cancel();

        ProgressBar node = lookup("#timerBar").queryFirst();
        assertTrue("Progress property is bound to the task we cancelled", node.progressProperty().isBound());
    }

    @Test
    public void testSetQuiz() throws Exception {
        String expected = quiz.getQuestion();
        Platform.runLater(() -> controller.setQuiz(quiz));
        Thread.sleep(10);

        Label node = lookup("#question").queryFirst();
        String actual = node.getText();

        assertEquals("Controller updated quiz text", expected, actual);
    }


    @Test
    public void testDoTransmit() throws Exception {
        Platform.runLater(() -> {
            controller.setQuiz(quiz);
            controller.doTransmitAnswer();
        });
        Thread.sleep(10);

        verifyThat("#input", hasText(""));
    }

    @Test
    public void testSetPlayer() throws Exception {
        Platform.runLater(() -> controller.setPlayer(player));
        Thread.sleep(10);

        String expected = player.getName();
        Label node = lookup("#playerName").queryFirst();
        String actual = node.getText();

        assertEquals("Player name was updated", expected, actual);

        node = lookup("#playerScore").queryFirst();
        expected = String.valueOf(player.getScore());
        actual = node.getText();

        assertEquals("Player score was updated", expected, actual);
    }

    @Test
    public void testDisconnect() throws Exception {
        FlowActionHandler mock = mock(FlowActionHandler.class);
        Field handler = QuizController.class.getDeclaredField("actionHandler");
        handler.setAccessible(true);
        handler.set(controller, mock);

        clickOn("#disconnect");

        verify(mock, times(1)).navigate(HomeController.class);
    }

}
