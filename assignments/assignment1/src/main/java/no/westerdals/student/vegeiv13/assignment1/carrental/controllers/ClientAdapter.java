package no.westerdals.student.vegeiv13.assignment1.carrental.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import no.westerdals.student.vegeiv13.assignment1.carrental.concurrent.ClientTask;

import java.util.concurrent.Phaser;

public class ClientAdapter extends VBox implements Runnable {

    ClientTask task = new ClientTask();

    private static final Phaser phaser = new Phaser(5);

    private String name;
    private ClientAdapterNotifier parent;

    private ProgressBar progress;

    private Label label;

    public ClientAdapter() {
        progress = new ProgressBar();
        label = new Label();
        this.getChildren().addAll(label, progress);
    }

    public void bind(final String name, ClientAdapterNotifier parent) {
        label.setText(name);
        this.name = name;
        this.parent = parent;
        progress.progressProperty().bind(task.progressProperty());
    }

    private void transition() {
        parent.transition(this, task.getClientState());
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        task.run();
        while(!Thread.currentThread().isInterrupted()) {

        }
    }

}
