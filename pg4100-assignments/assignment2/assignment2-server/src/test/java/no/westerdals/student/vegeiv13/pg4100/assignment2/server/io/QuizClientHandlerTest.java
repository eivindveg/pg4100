package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.QuizGenerator;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.BookService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.BookRepository;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.PlayerRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Year;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class QuizClientHandlerTest {

    private QuizClientHandler handler;
    private ChannelHandlerContext context;
    private PlayerService playerService;
    private BookService bookService;
    private BookRepository bookRepository;
    private PlayerRepository playerRepository;

    @Before
    public void setup() {
        ClassResolver classResolver = ClassResolvers.cacheDisabled(null);
        QuizGenerator quizGenerator = new QuizGenerator();
        playerService = new PlayerService();
        playerRepository = mock(PlayerRepository.class);
        playerService.setPlayerRepository(playerRepository);

        bookService = new BookService();
        bookRepository = mock(BookRepository.class);
        bookService.setBookRepository(bookRepository);
        handler = new QuizClientHandler(classResolver, quizGenerator, playerService, bookService);
        context = mock(ChannelHandlerContext.class);
    }


    /**
     * Unfortunately, I had to abandon this test. Originally, I had issues setting up the client without
     * Netty when my ObjectOutputStream kept throwing exceptions. Apparently, Netty is not compatible with standard
     * Java approaches to serialization. Some references:
     * http://rusya7.blogspot.no/2015/02/netty-server-is-incompatible-with.html
     * http://stackoverflow.com/questions/24979610/netty-defaultchannelpipeline-exceptioncaught
     * http://stackoverflow.com/questions/28634685/deserialize-an-object-with-camel-netty4
     * <p>
     * This means that I'd probably spend more time keeping my tests compatible with Netty, than I'd ever spend keeping
     * the code alive. This means that properly testing Netty code would be done with a "Specification Only" client
     * connecting to an integration server. However; that's at least 100 lines of codes that also have to be kept up to
     * date, and would be required only for version 1.0-RELEASE specs.
     * This feels bitter; I've spent some time managing my specs, but right now, I can't even mock Serialization properly
     * and this prevents me from testing the flow inside my handlers, because they'll break.
     */
    /*
    @Test
    public void testHandlerRespondsWithSavedPlayer() throws Exception {
        Player player = new Player("TestPlayer");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(player);
        out.flush();
        bos.flush();
        byte[] bytes = bos.toByteArray();
        System.out.println(bytes.length);
        handler.channelRead(context, Unpooled.copiedBuffer(bytes));

        verify(context, times(1)).write(player);
    }
    */
    @Test
    public void testReadPlayer() throws Exception {
        trainBookRepositoryMock();

        Player player = new Player("TestPlayer");

        handler.handlePlayer(context, player);

        verify(context, times(1)).writeAndFlush(player);
        verify(playerRepository, times(1)).saveAndFlush(player);
        verify(playerRepository, times(1)).findByName("TestPlayer");
        verify(bookRepository, times(1)).findRandom();
    }

    private void trainBookRepositoryMock() {
        when(bookRepository.findRandom()).thenReturn(Arrays.asList(
                new Book("TestAuthor", "TestTitle", "1234567890123", 100, Year.of(2000))
        ));
    }

    @Test
    public void testReadQuiz() throws Exception {
        trainBookRepositoryMock();
        Player testPlayer = new Player("TestPlayer");
        Quiz quiz1 = new Quiz("What is this?", "A test");

        Field field = QuizClientHandler.class.getDeclaredField("activeQuiz");
        field.setAccessible(true);
        field.set(handler, quiz1);

        field = QuizClientHandler.class.getDeclaredField("player");
        field.setAccessible(true);
        field.set(handler, testPlayer);

        int scoreBefore = testPlayer.getScore();
        int scoreExpected = scoreBefore + 1;
        handler.handleQuiz(context, quiz1);

        assertEquals("Player score was incremented by one", scoreExpected, testPlayer.getScore());

        verify(context, times(1)).writeAndFlush(testPlayer);
        verify(context, times(2)).writeAndFlush(any(Quiz.class));
    }

    @Test
    public void testSimplifyQuizAnswer() {
        Quiz quiz = new Quiz("What is this?", "A complex. test");
        String expected = "acomplextest";
        String actual = handler.simplifyQuizAnswer(quiz);
        assertEquals("The handler can simplify quiz answers to ignore spaces and punctuation", expected, actual);
    }

    @Test
    public void testHandlerSwallowsDisconnectionExceptions() throws Exception {
        IOException e = new IOException("forcibly closed by the remote host");
        handler.exceptionCaught(null, e);
    }

    @Test(expected = Exception.class)
    public void testHandlerDoesNotSwallowOtherExceptions() throws Exception {
        IOException e = new IOException("TestException");
        handler.exceptionCaught(null, e);
    }
}
