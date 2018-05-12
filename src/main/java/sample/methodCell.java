package sample;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;

public class methodCell extends ListCell<Method> {



    @Override
    protected void updateItem(Method item, boolean empty) {

        super.updateItem(item, empty);

        if(item != null && item.getParameterCount()==0)
        {//sets in green items that can Invoked
            this.setText(item.toString());
            this.setTextFill(Color.GREEN);

        }else if (item != null){
        //sets in red items that can Invoked
            this.setText(item.toString());
            this.setTextFill(Color.RED);

        }
    }


}
