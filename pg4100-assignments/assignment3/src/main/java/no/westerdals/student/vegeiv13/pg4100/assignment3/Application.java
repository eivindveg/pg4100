package no.westerdals.student.vegeiv13.pg4100.assignment3;

import io.datafx.controller.flow.Flow;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.pg4100.assignment3.window.Controller;

public class Application extends javafx.application.Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Flow flow = new Flow(Controller.class);

        StackPane start = flow.createHandler().start();
        Scene scene = new Scene(start);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
