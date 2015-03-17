package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import com.sun.istack.internal.Nullable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
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
        if(user == null || user.replaceAll(" ", "").equals("")) {
            setError("Invalid username");
            return;
        }
        String host = serverInput.getText();
        System.out.println("Connecting to " + host);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(new NioEventLoopGroup())
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(final Channel ch) throws Exception {
                            System.out.println("Init channel");
                            ObjectDecoder objectDecoder = new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader()));
                            ObjectEncoder objectEncoder = new ObjectEncoder();
                            ch.pipeline().addLast(objectDecoder, objectEncoder);
                        }
                    }).validate();
            ChannelFuture connection = bootstrap
                    .connect(host, Constants.PORT)
                    .sync()
                    .addListener(future -> {
                        Channel channel = ((ChannelFuture) future).channel();
                        System.out.println(channel.isOpen());
                        System.out.println(channel);
                        if(!channel.isActive()) {
                            Platform.runLater(() -> setError("Connection failed"));
                        } else {
                            Platform.runLater(() -> {
                                try {
                                    actionHandler.navigate(QuizController.class);
                                } catch (VetoException | FlowException e) {
                                    System.exit(0);
                                }
                            });
                        }

                    });
            context.getApplicationContext().register(connection);
        } catch (UnresolvedAddressException e) {
            e.printStackTrace();
            closeConnectionIfExists();
            setError("Could not connect");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setError(final @Nullable String message) {
        if (message == null) {
            errorLabel.setText(null);
        } else {
            errorLabel.setText(message);
        }
    }


    private void closeConnectionIfExists() {
        ChannelFuture registeredObject = context.getApplicationContext().getRegisteredObject(ChannelFuture.class);
        System.out.println(registeredObject);
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
