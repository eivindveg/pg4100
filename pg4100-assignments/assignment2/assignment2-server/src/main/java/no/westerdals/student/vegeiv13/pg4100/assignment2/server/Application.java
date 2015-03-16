package no.westerdals.student.vegeiv13.pg4100.assignment2.server;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(SocketConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
