package no.westerdals.student.vegeiv13.pg4100.assignment2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.quiz.QuizServerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;



@Configuration
public class SocketConfiguration {

    @Bean
    public ServerBootstrap serverBootstrap() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(group())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(final Channel ch) throws Exception {
                        ch.pipeline().addLast(new QuizServerHandler());
                    }
                });
        Channel channel = bootstrap.bind(Constants.PORT).sync().channel();

        return bootstrap;
    }

    @Bean
    public EventLoopGroup group() {
        return new NioEventLoopGroup();
    }


    @Bean
    public CountDownLatch latch() {
        return new CountDownLatch(1);
    }


}
