package es.grupo9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class HelloController{
    @FXML
    TextField Input1,Input2,Input3,Input4,Input5,Input6;
    @FXML
    Button id1,id2,id3,id4,Search;
    @FXML
    Pane Pane;
    @FXML
    TabPane TabPane1,TabPane2,TabPane3;

    /**
     * Handler for when the button is clicked
     * @param e Button click
     */
    @FXML
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.id1){Pane.toFront();
        }else if(e.getSource() == this.id2) TabPane1.toFront();
        else if(e.getSource() == this.id3) TabPane2.toFront();
        else if(e.getSource() == this.id4) TabPane3.toFront();
    }

    @FXML
    public void searching(ActionEvent e){
        if(e.getSource() == this.Search){
            String API_KEY = this.Input1.getText();
            String TOKEN = this.Input2.getText();
            String BOARD_ID = this.Input3.getText();

            TrelloManager trelloManager = new TrelloManager("e3ee0d6a1686b4b43ba5d046bbce20af", "80644fefce741495acc2f1ebf7174b536ae31a6c5c425622fbf5477f82463b84", "614de300aa6df33863299b6c");
            System.out.println(trelloManager.getSprintCount());
        }
    }
}


