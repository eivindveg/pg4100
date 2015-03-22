package no.westerdals.student.vegeiv13.pg4100.assignment2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectEncoder;
import no.westerdals.student.vegeiv13.pg4100.assignment2.Constants;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.QuizGenerator;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class SocketConfiguration {

    public static final String MODEL_PACKAGE = "no.westerdals.student.vegeiv13.pg4100.assignment2.models";

    /**
     * ClassResolver Bean for use in Netty. Used to map Serializable classes in classpath
     * @return ClassResolver
     */
    @Bean
    public ClassResolver classResolver() {
        return ClassResolvers.cacheDisabled(getClass().getClassLoader());
    }

    /**
     * EntityManagerFactory Bean. Contains a record of all active entities. Flags them dirty if they're changed.
     * @param dataSource DataSource Bean
     * @return LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan(MODEL_PACKAGE);
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    /**
     * Properties for use in Hibernate
     * @return Properties
     */
    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", org.hibernate.dialect.MySQL5Dialect.class.getName());

        return properties;
    }

    /**
     * TransactionManager Bean to allow Spring to Proxy our transactions in AOP
     * @param emf EntityManagerFactory Bean
     * @return JpaTransactionManager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    /**
     * SessionFactory Bean for Hibernate sessions, used in transaction management
     * @param dataSource DataSource Bean
     * @return SessionFactory
     */
    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder localSessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource);
        localSessionFactoryBuilder.scanPackages(MODEL_PACKAGE);

        return localSessionFactoryBuilder.buildSessionFactory();
    }

    /**
     * Netty EventLoopGroup that sets up relevant thread pools for client connections
     * @return NioEventLoopGroup
     */
    @Bean
    public EventLoopGroup group() {
        return new NioEventLoopGroup();
    }

    /**
     * QuizGenerator Bean
     * @return QuizGenerator
     */
    @Bean
    public QuizGenerator quizGenerator() {
        return new QuizGenerator();
    }

    /**
     * Set up a server bootstrap that acts as the socket server with its own thread pool.
     * @param channelInitializer ChannelInitializer Bean from the context
     * @return A ServerBootstrap to handle incoming requests
     */
    @Bean
    public ServerBootstrap serverBootstrap(ChannelInitializer channelInitializer) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group())
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer);
        bootstrap.bind(Constants.PORT).syncUninterruptibly().channel();

        return bootstrap;
    }

    /**
     * ObjectEncoder Bean to translate Serializable objects for Netty
     * @return ObjectEncoder
     */
    @Bean
    public ObjectEncoder objectEncoder() {
        return new ObjectEncoder();
    }

}
