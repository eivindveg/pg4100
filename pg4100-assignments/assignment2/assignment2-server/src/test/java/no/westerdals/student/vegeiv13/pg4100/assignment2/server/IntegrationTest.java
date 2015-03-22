package no.westerdals.student.vegeiv13.pg4100.assignment2.server;

import io.netty.bootstrap.ServerBootstrap;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.BookRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import static org.junit.Assert.*;

public class IntegrationTest {

    private static ConfigurableApplicationContext context;

    @BeforeClass
    public static void start() {
        SpringApplication application = new SpringApplication(ServerApplication.class);
        application.setAdditionalProfiles("test");
        context = application.run();
    }

    @AfterClass
    public static void stop() {
        if (context != null) {
            ServerBootstrap socketServer = context.getBean(ServerBootstrap.class);
            socketServer.group().shutdownGracefully();
            if (context.isActive()) {
                context.close();
            }
        }
    }

    @Test
    public void testDataSourceIsH2() {
        DataSource bean = context.getBean(DataSource.class);
        boolean embeddedDatabase = bean instanceof EmbeddedDatabase;
        assertTrue("DataSource bean is an embedded server", embeddedDatabase);
    }

    @Test
    public void testDatabaseIsSeeded() {
        BookRepository bookRepository = context.getBean(BookRepository.class);
        List<Book> all = bookRepository.findAll();
        boolean seeded = all.size() > 0;
        assertTrue("DataBase has been seeded with books", seeded);
    }

    @Test
    public void testServerAcceptsSocketConnection() throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(Constants.PORT));
        boolean connected = socket.isConnected();
        assertTrue("ServerSocket accepts connections on port " + Constants.PORT, connected);
    }

    @Test
    public void testSavingAPlayerSetsAnId() {
        Player player = new Player("TestPlayer");
        PlayerService playerService = context.getBean(PlayerService.class);
        assertNull("Unsaved player has no id", player.getId());
        playerService.save(player);
        assertNotNull("Saved player has an id", player);
    }
}
