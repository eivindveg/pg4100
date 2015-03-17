package no.westerdals.student.vegeiv13.pg4100.assignment2.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizServerHandler extends ChannelInboundHandlerAdapter {

    private Player player;

    private Quiz activeQuiz;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        System.out.println(bookRepository);
        final ByteBuf time = context.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = context.writeAndFlush(time);
        f.addListener(future -> context.close());
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) {

    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public void setBookRepository(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
