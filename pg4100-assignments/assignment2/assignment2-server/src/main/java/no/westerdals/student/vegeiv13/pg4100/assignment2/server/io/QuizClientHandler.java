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
        System.out.println(answer);
        String correctAnswer = simplifyQuizAnswer(activeQuiz);
        System.out.println(correctAnswer);

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
        System.out.println(activeQuiz.toString());
    }

    private String simplifyQuizAnswer(Quiz input) {
        String answer = input.getAnswer();
        return answer.replaceAll("\\.", "").replaceAll(" ", "").toLowerCase();
    }

    private void saveAndTransmitPlayer(final Player player, ChannelHandlerContext context) {
        boolean status = Player.validate(player);
        if (status) {
            playerService.save(player);
            if(this.player == null) {
                this.player = playerService.findByName(player.getName());
            }
        }
        context.writeAndFlush(this.player);
    }

}
