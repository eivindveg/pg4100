package no.westerdals.student.vegeiv13.pg4100.assignment2.server.quiz;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class QuizServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        final ByteBuf time = context.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = context.writeAndFlush(time);
        f.addListener(future -> context.close());
    }

    @Override
    public void channelRead(final ChannelHandlerContext context, Object payload) {

    }
}
