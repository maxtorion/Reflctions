package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.lang.reflect.Method;

public class Controller {

    public VBox getMainPane() {
        return mainPane;
    }

    @FXML
    private VBox mainPane = new VBox();

    @FXML
    private TableView<?> getANDsetTableVIew = new TableView<>();

    @FXML
    private ListView<Method> methodsListView = new ListView<>();

    @FXML
    private TextField classTextBox = new TextField();

    @FXML
    void textInputed(KeyEvent event) {
        //Firing method, which after Enter is pressed creates reflection and tasks specified in zpo lab 4
        //TODO: Populate Table View with methods
        if (event.getCode().equals(KeyCode.ENTER)) {

            try {
                Class reflectClass = Class.forName(this.classTextBox.getText());
                this.methodsListView.setItems(FXCollections.observableArrayList(reflectClass.getMethods()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            }
        }
    }
}
