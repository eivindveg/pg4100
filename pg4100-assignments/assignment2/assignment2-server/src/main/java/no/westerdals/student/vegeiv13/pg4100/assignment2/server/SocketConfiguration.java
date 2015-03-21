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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
    @Autowired
    private Environment environment;

    @Bean
    public ClassResolver classResolver() {
        return ClassResolvers.cacheDisabled(getClass().getClassLoader());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan(MODEL_PACKAGE);
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", org.hibernate.dialect.MySQL5Dialect.class.getName());

        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder localSessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource);
        localSessionFactoryBuilder.scanPackages(MODEL_PACKAGE);

        return localSessionFactoryBuilder.buildSessionFactory();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.pass"));

        return dataSource;
    }

    @Bean
    public EventLoopGroup group() {
        return new NioEventLoopGroup();
    }

    @Bean
    public QuizGenerator quizGenerator() {
        return new QuizGenerator();
    }

    /**
     * Set up a server bootstrap that acts as the socket server with its own thread pool.
     * @param channelInitializer
     * @return
     * @throws InterruptedException
     */
    @Bean
    public ServerBootstrap serverBootstrap(ChannelInitializer channelInitializer) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group())
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer);
        bootstrap.bind(Constants.PORT).sync().channel();

        return bootstrap;
    }

    @Bean
    public ObjectEncoder objectEncoder() {
        return new ObjectEncoder();
    }

}
