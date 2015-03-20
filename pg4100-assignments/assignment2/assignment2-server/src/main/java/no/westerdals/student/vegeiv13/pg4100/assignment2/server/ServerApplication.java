package no.westerdals.student.vegeiv13.pg4100.assignment2.server;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(SocketConfiguration.class)
@ComponentScan({
        "no.westerdals.student.vegeiv13.pg4100.assignment2.models",
        "no.westerdals.student.vegeiv13.pg4100.assignment2.server.io",
        "no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources"
})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
