package no.westerdals.student.vegeiv13.pg4100.assignment2.server;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({
        SocketConfiguration.class,
        DevelopmentConfiguration.class,
        ProductionConfiguration.class
})
@ComponentScan({
        "no.westerdals.student.vegeiv13.pg4100.assignment2.models",
        "no.westerdals.student.vegeiv13.pg4100.assignment2.server.io",
        "no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources"
})
public class ServerApplication {

    public static void main(String[] args) {
        String profile;
        if (args.length >= 1 && args[0] != null) {
            profile = args[0];
        } else {
            profile = "dev";
        }
        SpringApplication springApplication = new SpringApplication(ServerApplication.class);
        springApplication.setAdditionalProfiles(profile);
        springApplication.run();
    }
}
