package sample;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;

public class valueCell extends TableCell<Field, String> {
    Class reflectionClass;

    public valueCell(Class reflectionClass) {
        this.reflectionClass = reflectionClass;
    }

   

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty==false) {
            if(Misc.obtainSetter(this.getTableView().getItems().get(this.getIndex()).getName(),this.reflectionClass)!=null)
            {
                this.setEditable(true);
                this.setText(item.toString());
                this.setTextFill(Color.GREEN);
            }
            else
            {
                this.setText(item.toString());
                this.setTextFill(Color.RED);
            }
        }
    }
}
