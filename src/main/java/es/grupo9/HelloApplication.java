package es.grupo9;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * HelloApplication class. The main class that initializes the application.
 */
public class HelloApplication extends Application {

    /**
     * Launches the UI of the application, invoking the fxml file "hello-view" and
     * constructing the environment.
     *
     * @param stage Stage which you choose to initialize
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Dashboard SCRUM");
        stage.getIcons().add(new Image("https://i.imgur.com/K3ByTYR.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}