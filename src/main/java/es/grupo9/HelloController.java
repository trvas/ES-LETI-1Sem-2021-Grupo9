package es.grupo9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HelloController{
    @FXML
    Button id1;
    @FXML
    Button id2;
    @FXML
    Button id3;
    @FXML
    Button id4;

    /**
     * Handler for when the button is clicked
     * @param e Button click
     */
    @FXML
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.id1){
            System.out.println("teste");
        }
    }
}

