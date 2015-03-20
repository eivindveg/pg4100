package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.annotation.PostConstruct;

@FXMLController("./Quiz.fxml")
public class QuizController extends ObjectDecoder {

    @FXML
    private ProgressBar timerBar;

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @ActionHandler
    private FlowActionHandler actionHandler;

    private Player player;

    public QuizController() {
        super(ClassResolvers.cacheDisabled(null));
    }

    @PostConstruct
    public void init() {
        player = context.getRegisteredObject(Player.class);
        System.out.println(player);
        Channel channel = context.getRegisteredObject(NioSocketChannel.class);
        channel.pipeline().addFirst(this);
        ChannelFuture channelFuture = channel.writeAndFlush(player);
        channelFuture.syncUninterruptibly();
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        Object decode = decode(context, (ByteBuf) payload);

        System.out.println(decode);
    }
}
