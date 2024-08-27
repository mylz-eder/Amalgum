package controller;

import app.PersonApp;
import app.SimCardApp;
import app.ProductApp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private Button personBtn, simCardBtn, productBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personBtn.setOnAction(event -> openPersonApp());
        simCardBtn.setOnAction(event -> openSimCard());
        productBtn.setOnAction(event -> openProductApp());
    }

    private void openPersonApp () {
        new PersonApp().start(new Stage());
    }
    private void openSimCard () {
        new SimCardApp().start(new Stage());
    }
    private void openProductApp () {
        new ProductApp().start(new Stage());
    }
}
