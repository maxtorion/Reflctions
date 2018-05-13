package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class valueCell extends TextFieldTableCell<Field, String> {
    private Class reflectionClass;
    private Object reflectionInstance;
    private TextField textField;
    boolean escapePressed = false;
    private TablePosition<Field,
            ?> tablePos = null;

    public valueCell(Class reflectionClass, Object reflectionInstance) {
        this.reflectionClass = reflectionClass;
        this.reflectionInstance = reflectionInstance;
    }

    private TextField getTextField() {
        final TextField textField = new TextField(getItem());

        textField.setOnAction(event -> {
            System.out.println("Test");
        });

        textField.setOnAction(event -> {
            this.commitEdit(textField.getText());
            event.consume();
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    commitEdit(textField.getText());
                }
            }
        });
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ESCAPE)
                escapePressed = true;
            else
                escapePressed = false;
        });

        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                throw new IllegalArgumentException(
                        "did not expect esc key releases here.");
            }
        });

        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                textField.setText(getItem());
                cancelEdit();
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT ||
                    event.getCode() == KeyCode.TAB) {
                getTableView().getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                getTableView().getSelectionModel().selectPrevious();
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                getTableView().getSelectionModel().selectAboveCell();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                getTableView().getSelectionModel().selectBelowCell();
                event.consume();
            }
        });

        return textField;
    }

    private void startEdit(final TextField textField) {
        if (textField != null) {
            textField.setText(getItem());
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }

    private Object convert(Class<?> targetType, String value)
    {
        PropertyEditor editor = PropertyEditorManager.findEditor(targetType);
        editor.setAsText(value);
        return editor.getValue();
    }


    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() ||
                !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();
        if (isEditing()) {
            if (textField == null) {
                this.textField = getTextField();
            }
            escapePressed = false;
            startEdit(this.textField);
            final TableView<Field> table = getTableView();
            tablePos = table.getEditingCell();
        }
    }

    @Override
    public void commitEdit(String newValue) {
        if (!isEditing())
            return;
        final TableView<Field> table = getTableView();
        if (table != null) {
            // Inform the TableView of the edit being ready to be committed.
            TableColumn.CellEditEvent editEvent = new TableColumn.CellEditEvent(table, tablePos,
                    TableColumn.editCommitEvent(), newValue);

            //using setter to set new value within an object
            Method setter = Misc.obtainSetter(table.getItems().get(tablePos.getRow()).getName(), this.reflectionClass);
            try {
                setter.setAccessible(true);

                if (Modifier.isStatic(
                        setter.getModifiers()
                ))
                    setter.invoke(null, convert(setter.getParameterTypes()[0],newValue));
                else
                    setter.invoke(this.reflectionInstance, convert(setter.getParameterTypes()[0],newValue));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //----------------------------------------------

            Event.fireEvent(getTableColumn(), editEvent);
        }
        // we need to setEditing(false):
        super.cancelEdit(); // this fires an invalid EditCancelEvent.
        // update the item within this cell, so that it represents the new value
        updateItem(newValue, false);
        if (table != null) {
            // reset the editing cell on the TableView
            table.edit(-1, null);
        }
    }

    @Override
    public void cancelEdit() {
        if (escapePressed) {
            // this is a cancel event after escape key
            super.cancelEdit();
            setText(getItem()); // restore the original text in the view
        } else {
            // this is not a cancel event after escape key
            // we interpret it as commit.
            String newText = textField.getText();
            // commit the new text to the model
            this.commitEdit(newText);
        }
        setGraphic(null); // stop editing with TextField
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty == false && item!=null) {
            if (Misc.obtainSetter(this.getTableView().getItems().get(this.getIndex()).getName(), this.reflectionClass) != null) {
                this.setEditable(true);
                this.setText(item.toString());
                this.setTextFill(Color.GREEN);
            } else {
                this.setEditable(false);
                this.setText(item.toString());
                this.setTextFill(Color.RED);
            }
        }
    }
}
