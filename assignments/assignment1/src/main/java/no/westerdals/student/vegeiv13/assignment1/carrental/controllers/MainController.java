package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.context.FXMLViewFlowContext;
import org.datafx.controller.flow.context.ViewFlowContext;

import javax.annotation.PostConstruct;

@FXMLController("/window.fxml")
public class MainController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @PostConstruct
    public void init() {
        System.out.println(context.getRegisteredObject("names"));
    }
}
