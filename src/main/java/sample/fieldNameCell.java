package sample;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;

public class fieldNameCell extends TableCell<Field, String> {

    public fieldNameCell(Class reflectionClass) {
        this.reflectionClass=reflectionClass;
    }

    Class reflectionClass;



    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);



            if(empty!=true && Misc.obtainSetter(item,this.reflectionClass)!=null)
            {
                this.setText(item);
                this.setTextFill(Color.GREEN);
            }
            else{
                this.setText(item);
                this.setTextFill(Color.RED);
            }



    }
}
