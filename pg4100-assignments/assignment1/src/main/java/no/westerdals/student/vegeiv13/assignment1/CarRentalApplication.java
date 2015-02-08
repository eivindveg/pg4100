package no.westerdals.student.vegeiv13.assignment1;

import javafx.application.Application;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.assignment1.carrental.controllers.MainController;
import org.datafx.controller.flow.Flow;

public class CarRentalApplication extends Application {

    public static void main(String[] args) {
        Application.launch(CarRentalApplication.class, args);
    }

    /**
     * Starts the application on the main JavaFX thread.
     * @param primaryStage the window
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(MainController.class)
                .startInStage(primaryStage);
    }

}
