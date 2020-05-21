package gui.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

    public static Stage atualStage(Event event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }


    public static Integer converterInteiro(String msg) {
        try {
            return Integer.parseInt(msg);
        }catch (NumberFormatException e){
            return null;
        }
    }


}
