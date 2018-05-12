package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//TODO: differentiate methods in list view with colors
public class Controller {

    public VBox getMainPane() {
        return mainPane;
    }

    private Class reflectionClass;


    private Object reflectionInstance;

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
                this.reflectionClass = Class.forName(this.classTextBox.getText());
                this.reflectionInstance = this.reflectionClass.getConstructor().newInstance();


                this.methodsListView.setItems(FXCollections.observableArrayList(reflectionClass.getMethods()));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    public void initialize()
    {
        //to set some properties on initialization
        this.methodsListView.setCellFactory(new Callback<ListView<Method>, ListCell<Method>>() {
            @Override
            public ListCell<Method> call(ListView<Method> param) {
                return new methodCell();
            }
        });


    }


}
