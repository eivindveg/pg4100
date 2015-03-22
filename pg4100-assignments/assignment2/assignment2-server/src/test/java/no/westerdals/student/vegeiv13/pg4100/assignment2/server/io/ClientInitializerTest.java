package no.westerdals.student.vegeiv13.pg4100.assignment2.server.io;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectEncoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.QuizGenerator;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.BookService;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.PlayerService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ClientInitializerTest {

    private ClientInitializer initializer;

    @Before
    public void setup() {
        initializer = new ClientInitializer(
                mock(ObjectEncoder.class),
                mock(ClassResolver.class),
                new QuizGenerator(),
                new BookService(),
                new PlayerService()
        );
    }

    @Test
    public void testInitializerCreatesHandler() throws Exception {
        Channel mock = mock(Channel.class);
        ChannelPipeline pipeLine = mock(ChannelPipeline.class);
        when(mock.pipeline()).thenReturn(pipeLine);

        initializer.initChannel(mock);
        verify(pipeLine, times(1)).addLast(any(ObjectEncoder.class), any(QuizClientHandler.class));

    }
}
