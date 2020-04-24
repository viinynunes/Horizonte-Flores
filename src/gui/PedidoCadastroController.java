package gui;

import application.Main;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.ItemPedido;
import model.entities.Produto;
import model.services.ClienteServico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PedidoCadastroController implements Initializable {

    private ItemPedido itemPedido;

    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnApagarItem;
    @FXML
    private Hyperlink hyperlinkSelecionarCliente;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtLocalizaProduto;
    @FXML
    private TableView<Produto> tbvItemsPedidoPorduto;
    @FXML
    private TableColumn<Integer, Produto> tbcProdutoId;
    @FXML
    private TableColumn<String, Produto> tbcProdutoNome;
    @FXML
    private TableColumn<Double, ItemPedido> tbcTotalItem;

    public void onBtnSalvarAction(ActionEvent event){

    }

    public void onBtnCancelarAction(ActionEvent event){
        Utils.atualStage(event).close();
    }

    public void onBtnApagarItemAction(){

    }

    public void onHyperlinkSelecionarCliente(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);

        carregaDialog(parentStage, "/gui/ClienteListDialog.fxml");

    }

    public void setItemPedido(ItemPedido itemPedido){
        this.itemPedido = itemPedido;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Stage stage = (Stage) Main.getScene().getWindow();
        tbvItemsPedidoPorduto.prefHeightProperty().bind(stage.heightProperty());
    }

    public synchronized <T> void carregaDialog (Stage parentStage, String caminho){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            BorderPane vBox = loader.load();

            Stage dialog = new Stage();

            ClienteListDialogController controller = loader.getController();
            controller.setServico(new ClienteServico());
            controller.updateDataForm();

            dialog.setTitle("Selecionar Cliente");
            dialog.setScene(new Scene(vBox));
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
        }



    }

}
