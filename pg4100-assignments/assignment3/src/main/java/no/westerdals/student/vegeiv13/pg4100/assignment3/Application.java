package no.westerdals.student.vegeiv13.pg4100.assignment3;

import io.datafx.controller.flow.Flow;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment3.window.StartController;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        javafx.application.Application.launch(Application.class, args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        new Flow(StartController.class).startInStage(primaryStage);
        primaryStage.show();
    }
}
