package gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class LoadPage {
    public static synchronized <T> void carregaDialogTittledPane(Stage parentStage, String caminho, Consumer<T> init) {

        try {
            FXMLLoader loader = new FXMLLoader(LoadPage.class.getResource(caminho));
            TitledPane pane = loader.load();
            Stage dialog = new Stage();

            T controller = loader.getController();
            init.accept(controller);

            dialog.setTitle("Cadastro");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized <T> void carregaDialogVBox(Stage parentStage, String caminho, Consumer<T> init){

        try {
            FXMLLoader loader = new FXMLLoader(LoadPage.class.getResource(caminho));
            VBox pane = loader.load();
            Stage dialog = new Stage();

            T controller = loader.getController();
            init.accept(controller);

            dialog.setTitle("Cadastro");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
