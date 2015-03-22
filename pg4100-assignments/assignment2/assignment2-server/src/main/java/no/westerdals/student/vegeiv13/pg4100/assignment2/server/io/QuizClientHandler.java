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

/**
 * Client Handler object for Netty
 */
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

    /**
     * Called whenever the server receives data. Decodes the payload and passes it on to relevant methods
     *
     * @param context ChannelHandlerContext that allows us to write data back to the client
     * @param payload The ByteBuf object that was received
     * @throws Exception if the request fails, typically if the ByteBuf is corrupt
     */
    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        Object decode = decode(context, (ByteBuf) payload);
        if (decode instanceof Player) {
            handlePlayer(context, (Player) decode);
        } else if (decode instanceof Quiz) {
            handleQuiz(context, (Quiz) decode);
        }
    }

    /**
     * Reads a received player object, saves it to the database and responds with a quiz
     *
     * @param payload A player object
     * @throws ReflectiveOperationException if the QuizGenerator fails
     */
    protected void handlePlayer(final ChannelHandlerContext context, @NotNull Player payload) throws ReflectiveOperationException {
        if (player == null) {
            saveAndTransmitPlayer(payload, context);
            transmitNewQuiz(context);
        } else {
            context.writeAndFlush(payload);
        }
    }

    /**
     * Reads a received quiz object, comparing the answer of the received quiz with the answer of the previously sent
     * quiz, increasing the player's score if the answer is correct, before responding with a new quiz regardless
     *
     * @param payload A quiz object
     * @throws ReflectiveOperationException if the QuizGenerator fails
     */
    protected void handleQuiz(final ChannelHandlerContext context, @NotNull Quiz payload) throws ReflectiveOperationException {
        String answer = simplifyQuizAnswer(payload);
        String correctAnswer = simplifyQuizAnswer(activeQuiz);

        if (answer.equals(correctAnswer)) {
            player.setScore(player.getScore() + 1);
            saveAndTransmitPlayer(player, context);
        }
        transmitNewQuiz(context);
    }

    /**
     * Sends a new quiz to the player, using the book service and the quiz generator to get it
     *
     * @param context context to use when transmitting
     * @throws ReflectiveOperationException if the QuizGenerator fails
     */
    protected void transmitNewQuiz(final ChannelHandlerContext context) throws ReflectiveOperationException {
        Book randomBook = bookService.getRandom();
        activeQuiz = generator.fromObject(randomBook);
        context.writeAndFlush(activeQuiz.cloneNoAnswer());
    }

    /**
     * Turns a given Quiz' answer into a flattened String, to allow the Player some leeway in terms of punctuation and
     * spacing
     *
     * @param input Quiz to flatten answer for
     * @return Flattened answer
     */
    protected String simplifyQuizAnswer(Quiz input) {
        String answer = input.getAnswer();
        return answer.replaceAll("\\.", "").replaceAll(" ", "").toLowerCase();
    }

    /**
     * Saves the player to the database and transmits it to the player. If the player object is invalid, the client may
     * have been tampered with, and the connection is closed.
     *
     * @param player  Player to save
     * @param context Context to use for transmitting
     */
    protected void saveAndTransmitPlayer(Player player, ChannelHandlerContext context) {
        boolean status = Player.validate(player);
        if (status) {
            // New player connection
            if (this.player == null) {
                Player fromDb = playerService.findByName(player.getName());
                if (fromDb != null) {
                    player = fromDb;
                }
                this.player = player;
            }
            context.writeAndFlush(this.player);
            playerService.save(player);
        } else {
            context.channel().close();
        }
    }

    /**
     * Handles exceptions that occur in this handler. Notably, it swallows all closed connections so they don't produce
     * a stack trace in the log when the client forcibly disconnects
     *
     * @param ctx   Context to use for writing. Currently not used in this class
     * @param cause The exception that was caught
     * @throws Exception if we didn't suppress the cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // Swallow the exception if the connection is simply closed.
        if (cause instanceof IOException) {
            IOException e = (IOException) cause;
            if (e.getMessage().contains("forcibly closed by the remote host")) {
                return;
            }
        }
        super.exceptionCaught(ctx, cause);
    }

}
