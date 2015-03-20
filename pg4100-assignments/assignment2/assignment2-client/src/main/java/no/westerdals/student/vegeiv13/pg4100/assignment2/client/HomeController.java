package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import com.sun.istack.internal.Nullable;
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
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.util.VetoException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;

@FXMLController("./Home.fxml")
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
    @ActionTrigger("doConnect")
    private Button connect;

    @FXML
    private Label errorLabel;

    @ActionMethod("doConnect")
    public void connect() {
        String user = userInput.getText();
        if (user == null || user.replaceAll(" ", "").equals("")) {
            setError("Invalid username");
            return;
        }
        String host = serverInput.getText();
        System.out.println("Connecting to " + host);

        Bootstrap bootstrap = getBootstrap();
        try {
            ChannelFuture connection = bootstrap
                    .connect(host, Constants.PORT)
                    .sync();
            connection.addListener(getListener(user));
        } catch (UnresolvedAddressException e) {
            closeConnectionIfExists();
            setError("Could not connect");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private GenericFutureListener<Future<? super Void>> getListener(String name) {
        return future -> {
            Channel channel = ((ChannelFuture) future).channel();
            System.out.println(channel.isOpen());
            System.out.println(channel);
            if (!channel.isActive()) {
                Platform.runLater(() -> setError("Connection failed"));
            } else {
                Platform.runLater(() -> {
                    try {
                        Player player = new Player(name);
                        context.register(player);
                        System.out.println(channel.getClass());
                        context.register(channel);
                        actionHandler.navigate(QuizController.class);
                    } catch (VetoException | FlowException e) {
                        /*
                        There is no reasonable way to survive these exceptions, so we re-wrap them into a
                        RuntimeException. We either have no window and this exception is moot, a corrupt package
                        and this exception is moot, or we're under such strict security priveleges we shouldn't
                        even be allowed to start the application and this exception is moot.
                         */
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }

    private Bootstrap getBootstrap() {
        return new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(final Channel ch) throws Exception {
                        System.out.println("Init channel");
                        ObjectEncoder objectEncoder = new ObjectEncoder();
                        ch.pipeline().addLast(objectEncoder);
                    }
                }).validate();
    }

    private void setError(final @Nullable String message) {
        if (message == null) {
            errorLabel.setText(null);
        } else {
            errorLabel.setText(message);
        }
    }


    private void closeConnectionIfExists() {
        ChannelFuture registeredObject = context.getRegisteredObject(ChannelFuture.class);
        if (registeredObject != null) {
            System.out.println("Closing connection");
            registeredObject.channel().close();
        }
    }


    @PostConstruct
    public void init() throws IOException {
        closeConnectionIfExists();

        userInput.setOnAction(event -> serverInput.requestFocus());
        serverInput.setOnAction(event -> connect());
    }

}
