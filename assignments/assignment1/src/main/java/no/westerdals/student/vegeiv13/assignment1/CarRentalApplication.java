package no.westerdals.student.vegeiv13.assignment1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.westerdals.student.vegeiv13.assignment1.carrental.controllers.NameController;

import java.io.IOException;

public class CarRentalApplication extends Application {

    private ClassLoader classLoader;
    private FXMLLoader loader;
    private Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(CarRentalApplication.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        classLoader = this.getClass().getClassLoader();
        loader = new FXMLLoader();




        /*
        NameController nameController = loader.getController();
        List<String> names = nameController.getNames();
        List<Client> clients = new ArrayList<>();
        for(String name : names) {
            Client c = new Client(name);
        }
        */
    }

    private void startNames() throws IOException {
        loader.setLocation(classLoader.getResource("names.fxml"));
        NameController nameController = new NameController();
        loader.setRoot(nameController);
        Parent root = loader.load();
        Scene scene = new Scene(nameController);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Car Rental");
        primaryStage.show();
        nameController.initiate();
        System.out.println("Got it");
    }
}
