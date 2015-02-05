package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import org.datafx.controller.context.ViewContext;

public interface ClientAdapterNotifier {

    public void transition(ClientService adapter, ViewContext context);
}
