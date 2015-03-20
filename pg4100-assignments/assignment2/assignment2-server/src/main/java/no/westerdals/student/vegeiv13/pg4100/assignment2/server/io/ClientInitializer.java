package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectEncoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.BookService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientInitializer extends ChannelInitializer<Channel> {

    private ObjectEncoder encoder;
    private ClassResolver classResolver;
    private final BookService bookService;
    private final PlayerService playerService;

    @Autowired
    public ClientInitializer(ObjectEncoder encoder,
                             ClassResolver classResolver,
                             BookService bookService,
                             PlayerService playerService) {
        this.encoder = encoder;
        this.classResolver = classResolver;
        this.bookService = bookService;
        this.playerService = playerService;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ch.pipeline().addLast(encoder, new QuizClientHandler(classResolver, bookService, playerService));
    }
}
