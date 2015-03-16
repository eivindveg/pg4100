package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import javafx.stage.Stage;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowException;

public class Application extends javafx.application.Application {

    public static void main(String[] args) throws FlowException {
        javafx.application.Application.launch(Application.class, args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Flow flow = new Flow(HomeController.class);

        flow.startInStage(primaryStage);
    }
}
