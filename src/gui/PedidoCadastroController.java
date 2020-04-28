package gui;

import application.Main;
import gui.listeners.ClienteChangeListener;
import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Cliente;
import model.entities.Fornecedor;
import model.entities.ItemPedido;
import model.entities.Produto;
import model.services.ClienteServico;
import model.services.ProdutoServico;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PedidoCadastroController implements Initializable, ClienteChangeListener {

    private ItemPedido itemPedido;
    private ProdutoServico produtoServico;
    private FilteredList<Produto> filteredList;

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
    @FXML
    private TableView<Produto> tbvLocalizaProduto;
    @FXML
    private TableColumn<String, Produto> tbcLocalizaProdutoNome;
    @FXML
    private TableColumn<Produto, Fornecedor> tbcLocalizaProdutoFornecedor;

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
    public void setProdutoServico(ProdutoServico produtoServico){
        this.produtoServico = produtoServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Stage stage = (Stage) Main.getScene().getWindow();
        tbvItemsPedidoPorduto.prefHeightProperty().bind(stage.heightProperty());

        tbcLocalizaProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcLocalizaProdutoFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

    }

    public void updateFormLocalizaProduto(){
        List<Produto> list = produtoServico.findAll();
        ObservableList<Produto> obbList = FXCollections.observableArrayList(list);
        filteredList = filteredTableView(obbList);

        if (filteredList.isEmpty() || filteredList == null){
            tbvLocalizaProduto.setVisible(false);
        } else {
            tbvLocalizaProduto.setItems(filteredList);
        }

    }

    public synchronized <T> void carregaDialog (Stage parentStage, String caminho){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            BorderPane vBox = loader.load();

            Stage dialog = new Stage();

            ClienteListDialogController controller = loader.getController();
            controller.setServico(new ClienteServico());
            controller.subscribeDataChangeListener(this);
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

    private FilteredList<Produto> filteredTableView(ObservableList<Produto> obbProdutoList) {
        FilteredList<Produto> filteredList = new FilteredList<>(obbProdutoList);

        txtLocalizaProduto.textProperty().addListener(((observable, oldValue, newValue) -> {

            this.filteredList.setPredicate(produto -> {
                if (newValue == null || newValue.isEmpty()){
                    tbvLocalizaProduto.setVisible(false);
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (produto.getNome().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    tbvLocalizaProduto.setVisible(true);
                    return true;
                } else {
                    return false;
                }
            });
        }));


        return filteredList;
    }

    @Override
    public void onClienteChanged(Cliente cliente) {
        hyperlinkSelecionarCliente.setText(cliente.getNome());
    }
}
