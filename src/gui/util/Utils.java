package gui.util;

import application.Main;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Utils {

    public static Stage atualStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Stage atualStage(MouseEvent event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Stage atualStage(KeyEvent event){
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
