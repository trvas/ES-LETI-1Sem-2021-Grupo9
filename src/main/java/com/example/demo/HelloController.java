package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    Button test;
    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: #AED6F1;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: #FF0000;";

    @FXML
    public void handleButtonPress() {
        System.out.println("1234");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        test.setOnMouseEntered(e -> test.setStyle(HOVERED_BUTTON_STYLE));
        test.setOnMouseExited(e -> test.setStyle(IDLE_BUTTON_STYLE));
    }
}

