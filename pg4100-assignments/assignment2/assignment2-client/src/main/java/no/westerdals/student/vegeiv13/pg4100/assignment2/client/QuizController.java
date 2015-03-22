package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@ViewController(value = "./Quiz.fxml", title = "Quiz - In game")
public class QuizController extends ObjectDecoder {

    @FXML
    private ProgressBar timerBar;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXML
    private Label question;
    @FXML
    private Label playerName;
    @FXML
    private Label playerScore;
    @FXML
    private TextField input;
    @FXML
    @ActionTrigger("transmit")
    private Button submit;
    @FXML
    @ActionTrigger("disconnect")
    private Button disconnect;

    private Player player;
    private Quiz quiz;

    private StringProperty playerNameProperty;
    private StringProperty quizLabelProperty;
    private StringProperty playerScoreProperty;
    private Channel channel;
    private Task<Void> task;

    public QuizController() {
        super(ClassResolvers.cacheDisabled(null));
    }

    @PostConstruct
    public void init() {
        transmitInitial();
        playerNameProperty = playerName.textProperty();
        quizLabelProperty = question.textProperty();
        playerScoreProperty = playerScore.textProperty();

        input.requestFocus();
        input.setOnAction((event) -> doTransmitAnswer());
    }

    @ActionMethod("transmit")
    private void doTransmitAnswer() {
        quiz.setAnswer(input.textProperty().get());
        transmit(quiz);
        input.clear();
    }

    private void transmitInitial() {
        player = context.getRegisteredObject(Player.class);
        channel = context.getRegisteredObject(NioSocketChannel.class);
        System.out.println(channel);
        channel.pipeline().addFirst(this);
        ChannelFuture channelFuture = channel.writeAndFlush(player);
        channelFuture.syncUninterruptibly();
    }

    private void transmit(Object object) {
        if(task.isRunning()) {
            task.cancel();
        }
        channel.writeAndFlush(object).syncUninterruptibly();
    }

    @ActionMethod("disconnect")
    private void doDisconnect() throws InterruptedException {
        if (channel.isActive()) {
            channel.disconnect();
        }
        if (channel.isOpen()) {
            channel.close();
        }
        try {
            actionHandler.navigate(HomeController.class);
        } catch (VetoException | FlowException e) {
            /*
            There is no reasonable way to survive these exceptions, so we re-wrap them into a
            RuntimeException. We either have no window and this exception is moot, a corrupt package
            and this exception is moot, or we're under such strict security priveleges we shouldn't
            even be allowed to start the application and this exception is moot.
             */
            throw new RuntimeException(e);
        }
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        Object decode = decode(context, (ByteBuf) payload);
        if (decode instanceof Player) {
            Platform.runLater(() -> setPlayer((Player) decode));
        } else if (decode instanceof Quiz) {
            Platform.runLater(() -> setQuiz((Quiz) decode));
        }
    }

    public void setPlayer(final @NotNull Player player) {
        playerNameProperty.setValue(player.getName());
        playerScoreProperty.setValue(String.valueOf(player.getScore()));
        this.player = player;
    }

    public void setQuiz(final @NotNull Quiz quiz) {
        quizLabelProperty.setValue(quiz.getQuestion());
        resetTimer();
        this.quiz = quiz;
    }

    private void resetTimer() {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long start = System.currentTimeMillis();
                long diff;
                do {
                    long now = System.currentTimeMillis();
                    diff = now - start;
                    updateProgress(Constants.TIME_LIMIT - diff, Constants.TIME_LIMIT);
                } while(diff < Constants.TIME_LIMIT);

                succeeded();
                return null;
            }
        };
        timerBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(value -> doTransmitAnswer());
        new Thread(task).start();
    }
}
