package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));
        primaryStage.setTitle("Reflections");

        primaryStage.setScene(new Scene(root, controller.getMainPane().getMinWidth(), controller.getMainPane().getMinHeight()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
