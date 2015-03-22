package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectEncoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.QuizGenerator;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.BookService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Netty component to set up a handler for each client connection
 */
@Component
public class ClientInitializer extends ChannelInitializer<Channel> {

    private final BookService bookService;
    private final PlayerService playerService;
    private ObjectEncoder encoder;
    private ClassResolver classResolver;
    private QuizGenerator generator;

    @Autowired
    public ClientInitializer(ObjectEncoder encoder,
                             ClassResolver classResolver,
                             QuizGenerator generator,
                             BookService bookService,
                             PlayerService playerService) {
        this.encoder = encoder;
        this.classResolver = classResolver;
        this.generator = generator;
        this.bookService = bookService;
        this.playerService = playerService;
    }

    /**
     * Initiates the given channel by adding an encoder(turns objects into data) and a QuizClientHandler(responds to
     * requests)
     *
     * @param ch Channel to bind to
     * @throws Exception if setting up fails
     */
    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(encoder, new QuizClientHandler(classResolver, generator, playerService, bookService));
    }
}
