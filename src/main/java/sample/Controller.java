package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

//TODO: Finish the list view by calling getters, and making fields editable if they have seters
//TODO: Add some communicats with returned values of invoked methods and exceptions
public class Controller {

    public VBox getMainPane() {
        return mainPane;
    }

    private Class reflectionClass;


    private Object reflectionInstance;

    @FXML
    private VBox mainPane = new VBox();

    @FXML
    private TableView<Field> getANDsetTableVIew = new TableView<>();

    @FXML
    private ListView<Method> methodsListView = new ListView<>();

    @FXML
    private TextField classTextBox = new TextField();

    @FXML
    private TableColumn<?, ?> fieldNameColumn;

    @FXML
    private TableColumn<?, ?> valueColumn;

    @FXML
    void textInputed(KeyEvent event) {
        //Firing method, which after Enter is pressed creates reflection and tasks specified in zpo lab 4



        if (event.getCode().equals(KeyCode.ENTER)) {

            try {
                this.reflectionClass = Class.forName(this.classTextBox.getText());
                this.reflectionInstance = this.reflectionClass.newInstance();
                this.methodsListView.setItems(FXCollections.observableArrayList(reflectionClass.getMethods()));



                ObservableList<Field> avaibleFields = FXCollections.observableArrayList();

                for (Field field:this.reflectionClass.getDeclaredFields()) {

                    if(checkIfGetterExists(field.getName(),this.reflectionClass))
                        avaibleFields.add(field);

                }

                this.getANDsetTableVIew.setItems(avaibleFields);



            } catch (ClassNotFoundException e) {
                failedCreationMessage(e);

            } catch (IllegalAccessException e) {
                failedCreationMessage(e);
            } catch (InstantiationException e) {
               failedCreationMessage(e);
            }
        }
    }
    private void failedCreationMessage(Exception e)
    {
        System.out.println("Cannot create instance of the class: "+ this.reflectionClass.getName()+"\n"+e.toString());
        System.out.println("Class annotations: ");
        Annotation [] ann = this.reflectionClass.getAnnotations();
        for (Annotation a:ann
             ) {
            System.out.format(" %s%n ", a.toString());
        }
        System.out.println("Is interface?: "+this.reflectionClass.isInterface());
        System.out.println("Is abstract?: " + Modifier.isAbstract(this.reflectionClass.getModifiers()));
        System.out.println("Is static?: "+Modifier.isStatic(this.reflectionClass.getModifiers()));
        System.out.println("Is private?: "+Modifier.isPrivate(this.reflectionClass.getModifiers()));
        System.out.println("Is protected?: "+Modifier.isProtected(this.reflectionClass.getModifiers()));

    }

    private boolean checkIfGetterExists(String fieldName, Class classReflection)
    {
       final String getterName = fieldName.replace(fieldName.charAt(0),Character.toUpperCase(fieldName.charAt(0)));
       long getters = Arrays.asList(classReflection.getMethods()).stream().filter(method -> method.getName().equals("get"+getterName)).count();

       if (getters > 0)
           return true;
       else
           return false;
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
        //event handler to invoke method after two mouse clicks on it
        this.methodsListView.setOnMouseClicked(event ->
        {
            if(event.getClickCount()==2)
            {
                if(this.methodsListView.getSelectionModel().getSelectedItem().getParameterCount()==0)
                {
                    Method method = this.methodsListView.getSelectionModel().getSelectedItem();
                    method.setAccessible(true);
                    try {
                        System.out.println("Udało się wywołać metodę: "+method.getName());

                        if(!Modifier.isStatic(method.getModifiers()))
                        System.out.println(method.invoke(this.reflectionInstance));

                        else
                            System.out.println(method.invoke(null));

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }



        });
        //configuration of tableColumns
        TableColumn<Field,String> fieldStringTableColumn = (TableColumn<Field, String>) this.getANDsetTableVIew.getColumns().get(0);

        fieldStringTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Field, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Field, String> param) {
                param.getValue().setAccessible(true);
                return new SimpleStringProperty(param.getValue().getName());
            }
        });
        fieldStringTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Field,String> valueStringTableColum = (TableColumn<Field, String>) this.getANDsetTableVIew.getColumns().get(1);

        valueStringTableColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Field, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Field, String> param) {
                try {
                    param.getValue().setAccessible(true);
                    if(Modifier.isStatic(param.getValue().getModifiers()))
                    return new SimpleStringProperty(param.getValue().get(null).toString());
                    else
                        return new SimpleStringProperty(param.getValue().get(reflectionInstance).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
        valueStringTableColum.setCellFactory(TextFieldTableCell.forTableColumn());
        
        
        
        

    }


}
