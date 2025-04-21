package ak;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import ak.Authentication.PasswordUtils;
import ak.admins.AdminManager;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("signin"), 1024, 768); 
        stage.setScene(scene);
        stage.setTitle("Top Bank"); 
        stage.setMinWidth(800); // Optional: Set a minimum width
        stage.setMinHeight(600); // Optional: Set a minimum height
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    
    public static <T> T getController() {
        return fxmlLoader.getController();
    }
    
    public static void main(String[] args) {
        launch();
    }


}