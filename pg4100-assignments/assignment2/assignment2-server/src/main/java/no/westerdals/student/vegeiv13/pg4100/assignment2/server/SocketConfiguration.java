package no.westerdals.student.vegeiv13.pg4100.assignment2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.handler.QuizServerHandler;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;



@Configuration
@EnableJpaRepositories
public class SocketConfiguration {

    private final List<Class> allowedClasses = Arrays.asList(
            no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player.class,
            no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book.class,
            no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz.class
    );

    @Autowired
    private Environment environment;

    /**
     * Not a proper class resolver; it only speaks in terms of specifically known and allowed classes.
     * @return
     */
    @Bean
    public ClassResolver classResolver() {
        return className -> {
            Class forName = Class.forName(className);
            if(allowedClasses.contains(forName)) {
                return forName;
            } else {
                throw new ClassNotFoundException("Class " + className + " not found or not supported");
            }
        };
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("no.westerdals.student.vegeiv13.pg4100.assignment2.models");
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if(environment.acceptsProfiles("development")) {
            dataSource.setDriverClassName(org.h2.Driver.class.getName());
            dataSource.setUrl("jdbc:h2:mem:test");
        } else {
            dataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));
            dataSource.setUrl(environment.getProperty("spring.datasource.url"));
            dataSource.setPassword(environment.getProperty("spring.datasource.pass"));
        }

        return dataSource;
    }

    @Bean
    public EventLoopGroup group() {
        return new NioEventLoopGroup();
    }


    @Bean
    public ServerBootstrap serverBootstrap(BookRepository bookRepository) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(final Channel ch) throws Exception {
                        QuizServerHandler quizServerHandler = new QuizServerHandler();
                        quizServerHandler.setBookRepository(bookRepository);
                        ch.pipeline().addLast(new ObjectEncoder(),  new ObjectDecoder(classResolver()), quizServerHandler);
                    }
                });
        bootstrap.bind(Constants.PORT).sync().channel();

        return bootstrap;
    }

}
