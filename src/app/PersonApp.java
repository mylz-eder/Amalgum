package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PersonApp {

    public void start(Stage primaryStage) {

        try {

            // setup FXMLLoader setup

            Parent root1 = FXMLLoader.load(getClass().getResource("/view/person.fxml"));
            Scene scene1 = new Scene(root1);

            // deploy the scene

            primaryStage.setScene(scene1);
            primaryStage.setTitle("Person Application");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
