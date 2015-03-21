package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

import com.sun.istack.internal.NotNull;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.QuizGenerator;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.BookService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class QuizClientHandler extends ObjectDecoder {

    private Player player;

    private Quiz activeQuiz;


    private QuizGenerator generator;
    private PlayerService playerService;
    private BookService bookService;

    public QuizClientHandler(final ClassResolver classResolver, QuizGenerator generator, PlayerService playerService,
                             BookService bookService) {
        super(classResolver);
        this.generator = generator;
        this.playerService = playerService;
        this.bookService = bookService;
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) throws Exception {
        super.channelActive(context);
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        Object decode = decode(context, (ByteBuf) payload);
        System.out.println("Reading channel");
        if (decode instanceof Player) {
            channelRead(context, (Player) decode);
        } else if (decode instanceof Quiz) {
            channelRead(context, (Quiz) decode);
        } else {
            System.out.println("What's this?");
        }
    }

    public void channelRead(final ChannelHandlerContext context, Player payload) throws Exception {
        if (player == null) {
            welcomePlayer(payload, context);
            transmitNewQuiz(context);
        } else {
            context.writeAndFlush(payload);
        }
    }

    private void transmitNewQuiz(final ChannelHandlerContext context) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        Book randomBook = bookService.getRandom();
        activeQuiz = generator.fromObject(randomBook);
        context.writeAndFlush(activeQuiz.cloneNoAnswer());
        System.out.println(activeQuiz.toString());
    }

    public void channelRead(final ChannelHandlerContext context, @NotNull Quiz payload) throws Exception {
        if(payload.getAnswer().equals(activeQuiz.getAnswer())) {
            player.setScore(player.getScore() + 1);
            playerService.save(player);
            context.writeAndFlush(player);
        }
        transmitNewQuiz(context);
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
