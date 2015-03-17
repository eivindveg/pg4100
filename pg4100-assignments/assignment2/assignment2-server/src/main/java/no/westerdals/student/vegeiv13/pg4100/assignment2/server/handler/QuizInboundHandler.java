package no.westerdals.student.vegeiv13.pg4100.assignment2.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizInboundHandler extends ObjectDecoder {

    private Player player;

    private Quiz activeQuiz;

    @Autowired
    private BookRepository bookRepository;

    public QuizInboundHandler(final ClassResolver classResolver) {
        super(classResolver);
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) throws Exception {
        System.out.println("Got connection");
        super.channelActive(context);
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) throws Exception {
        System.out.println("Reading channel");
        System.out.println(payload);
        if (payload instanceof Quiz) {
            Quiz quiz = (Quiz) payload;
            System.out.println(quiz);
        } else if (payload instanceof String) {
            System.out.println("Got a string: " + payload);
        }
        super.channelRead(context, payload);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("Decoding");
        System.out.println(in);
        Object decode = super.decode(ctx, in);
        System.out.println(decode);
        return decode;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public void setBookRepository(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
