package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProductApp {

    public void start(Stage primaryStage) {

        try {

            // setup FXMLLoader setup

            Parent root2 = FXMLLoader.load(getClass().getResource("/view/product.fxml"));
            Scene scene2 = new Scene(root2);

            // deploy the scene

            primaryStage.setScene(scene2);
            primaryStage.setTitle("Main Application");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}