package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import org.datafx.controller.FXMLController;

@FXMLController("./Quiz.fxml")
public class QuizController {

    @FXML
    private ProgressBar timerBar;

    public QuizController() {
        System.out.println("QuizController");
    }
}
