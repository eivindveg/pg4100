package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import no.westerdals.student.vegeiv13.assignment1.carrental.ClientState;

public interface ClientAdapterNotifier {

    void transition(ClientAdapter adapter, ClientState state);
}
