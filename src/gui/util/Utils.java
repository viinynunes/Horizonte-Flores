package gui.util;

import application.Main;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

    public static Stage atualStage(ActionEvent event) {
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
