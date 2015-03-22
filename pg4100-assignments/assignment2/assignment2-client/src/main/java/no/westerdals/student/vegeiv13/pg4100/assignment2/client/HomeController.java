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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * Class used to control the login screen and initiate server connections
 */
@ViewController(value = "./Home.fxml", title = "Quiz - Log in")
public class HomeController {

    @FXML
    private TextField userInput;

    @FXML
    private TextField serverInput;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @ActionHandler
    private FlowActionHandler actionHandler;

    @FXML
    @ActionTrigger("connect")
    private Button connect;

    @FXML
    private Label errorLabel;
    private Bootstrap bootstrap;

    /**
     * Attempts to connect to the server indicated by the host field, using the username indicated by the user input
     * field, after the connect button has been clicked
     */
    @SuppressWarnings("ConstantConditions")
    @ActionMethod("connect")
    public void doConnect() {
        String user = userInput.getText();
        if (user == null || user.replaceAll(" ", "").equals("")) {
            setError(Constants.INVALID_USERNAME);
            return;
        }
        String host = serverInput.getText();

        // Catching old-style and rethrowing exceptions we don't want because Netty is hiding our exceptions and we
        // want to present them in the gui layer
        try {
            ChannelFuture connection = bootstrap
                    .connect(host, Constants.PORT)
                    .syncUninterruptibly();
            connection.addListener(getListener(user));
        } catch (Exception e) {
            if (e instanceof ConnectException || e instanceof UnknownHostException) {
                setError(Constants.COULD_NOT_CONNECT);
            } else {
                throw e;
            }
        }
    }

    /**
     * Sets up a listener that handles the application context based on the result of a client connection
     *
     * @param name The player name to transmit upon a successful connection
     * @return A listener that navigates us forward upon success, or sets an error if we fail to connect
     */
    private GenericFutureListener<Future<? super Void>> getListener(String name) {
        return future -> {
            Channel channel = ((ChannelFuture) future).channel();
            if (!channel.isActive()) {
                Platform.runLater(() -> setError(Constants.COULD_NOT_CONNECT));
            } else {
                Platform.runLater(() -> {
                    try {
                        Player player = new Player(name);
                        context.register(player);
                        context.register(channel);
                        actionHandler.navigate(QuizController.class);
                    } catch (VetoException | FlowException e) {
                        /*
                        There is no reasonable way to survive these exceptions, so we re-wrap them into a
                        RuntimeException. We either have no window and this exception is moot, a corrupt package
                        and this exception is moot, or we're under such strict security priveleges we shouldn't
                        even be allowed to start the application and this exception is moot. Unfortunately, we're in
                        a lambda expression and have to handle this somehow.
                         */
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

    /**
     * Set up a server bootstrap if there is none in the context
     *
     * @return a Netty client Bootstrap
     */
    private Bootstrap getBootstrap() {
        Bootstrap bootstrap = context.getRegisteredObject(Bootstrap.class);
        if (bootstrap == null) {
            bootstrap = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(new NioEventLoopGroup())
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(final Channel ch) throws Exception {
                            ObjectEncoder objectEncoder = new ObjectEncoder();
                            ch.pipeline().addLast(objectEncoder);
                        }
                    }).validate();
            context.register(bootstrap);
        }
        return bootstrap;
    }

    /**
     * Sets the error label to display the given message
     *
     * @param message String to display - can be null
     */
    private void setError(final String message) {
        errorLabel.setText(message);
    }

    /**
     * Binds GUI components to their functions
     */
    @PostConstruct
    public void init() {
        bootstrap = getBootstrap();

        userInput.setOnAction(event -> serverInput.requestFocus());
        serverInput.setOnAction(event -> doConnect());
    }

}
