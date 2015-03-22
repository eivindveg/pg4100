package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

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

import javax.validation.constraints.NotNull;
import java.io.IOException;

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
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        Object decode = decode(context, (ByteBuf) payload);
        if (decode instanceof Player) {
            readPlayer(context, (Player) decode);
        } else if (decode instanceof Quiz) {
            readQuiz(context, (Quiz) decode);
        }
    }

    public void readPlayer(final ChannelHandlerContext context, Player payload) throws Exception {
        if (player == null) {
            saveAndTransmitPlayer(payload, context);
            transmitNewQuiz(context);
        } else {
            context.writeAndFlush(payload);
        }
    }

    public void readQuiz(final ChannelHandlerContext context, @NotNull Quiz payload) throws Exception {
        String answer = simplifyQuizAnswer(payload);
        String correctAnswer = simplifyQuizAnswer(activeQuiz);

        if (answer.equals(correctAnswer)) {
            player.setScore(player.getScore() + 1);
            saveAndTransmitPlayer(player, context);
        }
        transmitNewQuiz(context);
    }

    private void transmitNewQuiz(final ChannelHandlerContext context) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        Book randomBook = bookService.getRandom();
        activeQuiz = generator.fromObject(randomBook);
        context.writeAndFlush(activeQuiz.cloneNoAnswer());
    }

    private String simplifyQuizAnswer(Quiz input) {
        String answer = input.getAnswer();
        return answer.replaceAll("\\.", "").replaceAll(" ", "").toLowerCase();
    }

    private void saveAndTransmitPlayer(Player player, ChannelHandlerContext context) {
        boolean status = Player.validate(player);
        if (status) {
            // New player connection
            if(this.player == null) {
                Player fromDb = playerService.findByName(player.getName());
                if(fromDb != null) {
                    player = fromDb;
                }
                this.player = player;
            }
            context.writeAndFlush(this.player);
            playerService.save(player);
        } else {
            context.channel().close();
        }
        System.out.println(player);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // Swallow the exception if the connection is simply closed.
        if(cause instanceof IOException) {
            IOException e = (IOException) cause;
            if(e.getMessage().contains("forcibly closed by the remote host")) {
                return;
            }
        }
        super.exceptionCaught(ctx, cause);
    }

}
