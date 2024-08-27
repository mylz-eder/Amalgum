package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimCardApp {

    public void start(Stage primaryStage) {

        try {

            // setup FXMLLoader setup

            Parent root3 = FXMLLoader.load(getClass().getResource("/view/simCard.fxml"));
            Scene scene3 = new Scene(root3);

            // deploy the scene

            primaryStage.setScene(scene3);
            primaryStage.setTitle("Main Application");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
