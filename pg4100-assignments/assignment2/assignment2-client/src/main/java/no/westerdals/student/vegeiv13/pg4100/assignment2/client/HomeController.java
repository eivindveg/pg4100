package no.westerdals.student.vegeiv13.pg4100.assignment2.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.annotation.PostConstruct;
import java.io.IOException;

@FXMLController("./Home.fxml")
public class HomeController {

    @FXML
    private TextField userInput;

    @FXML
    private TextField serverInput;

    @FXMLViewFlowContext
    private ViewFlowContext context;


    @PostConstruct
    public void init() throws IOException {

    }

}
