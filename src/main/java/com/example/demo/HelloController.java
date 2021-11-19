package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


//hover css?
public class HelloController implements Initializable{
    @FXML
    Button id1;
    @FXML
    Button id2;
    @FXML
    Button id3;
    @FXML
    Button id4;


    @FXML
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.id1){
            System.out.println("teste");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

