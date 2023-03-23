package lk.ijse.dep10.little_john;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainView.fxml"))));
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to load the login Form").showAndWait();
            throw new RuntimeException(e);
        }
        primaryStage.setTitle("Main Login Form");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
