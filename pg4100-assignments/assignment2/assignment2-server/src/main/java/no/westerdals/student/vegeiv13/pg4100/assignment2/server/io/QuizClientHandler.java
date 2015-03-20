package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.BookService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class QuizClientHandler extends ObjectDecoder {

    private Player player;

    private Quiz activeQuiz;

    private BookService bookService;

    private PlayerService playerService;

    public QuizClientHandler(final ClassResolver classResolver, BookService bookService, PlayerService playerService) {
        super(classResolver);
        this.bookService = bookService;
        this.playerService = playerService;
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        Object decode = decode(context, (ByteBuf) payload);
        Class clazz = decode.getClass();
        System.out.println(clazz.getSimpleName());
        System.out.println("Reading channel");
        switch (clazz.getSimpleName()) {
            case "Player":
                channelRead(context, (Player) decode);
                break;
            case "Quiz":
                channelRead(context, (Quiz) decode);
                break;
            default:
                System.out.println("What's this?");
        }
    }

    public void channelRead(final ChannelHandlerContext context, Player payload) throws Exception {
        if (player == null) {
            welcomePlayer(payload, context);
        } else {
            context.writeAndFlush(payload);
        }
    }

    public void channelRead(final ChannelHandlerContext context, Quiz payload) throws Exception {

    }

    private void welcomePlayer(final Player player, ChannelHandlerContext context) {
        boolean status = Player.verify(player);
        if (status) {
            Player byName = playerService.findByName(player.getName());
            System.out.println(byName);
            if (byName == null) {
                System.out.println("New player");
                this.player = player;
                Player save = playerService.save(player);
                System.out.println(save);
            } else {
                System.out.println("Got player from db");
                this.player = byName;
            }
        }
        context.writeAndFlush(this.player);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }

}
