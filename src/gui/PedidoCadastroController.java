package gui;

import application.Main;
import db.DBException;
import gui.listeners.ClienteChangeListener;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.*;
import model.services.ClienteServico;
import model.services.ItemPedidoServico;
import model.services.ProdutoServico;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PedidoCadastroController implements Initializable, ClienteChangeListener {

    private ItemPedidoServico itemPedidoServico;
    private ProdutoServico produtoServico;
    private FilteredList<Produto> filteredList;
    private List<Produto> listProdutosSelecionados = new ArrayList<>();
    private Cliente cliente;
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
    @FXML
    private TableView<Produto> tbvLocalizaProduto;
    @FXML
    private TableColumn<String, Produto> tbcLocalizaProdutoNome;
    @FXML
    private TableColumn<Produto, Fornecedor> tbcLocalizaProdutoFornecedor;

    public void onBtnSalvarAction(ActionEvent event){

        try {
            itemPedido = getDataForm();
            itemPedidoServico.insert(itemPedido);
            Utils.atualStage(event).close();
        } catch (DBException e){
            Alerts.showAlert("Erro ao salvar pedido", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtnCancelarAction(ActionEvent event){
        Utils.atualStage(event).close();
    }

    public void onBtnApagarItemAction(){
        listProdutosSelecionados.remove(tbvItemsPedidoPorduto.getSelectionModel().getSelectedItem());
        updateFormProdutosPedido();
    }

    public void onHyperlinkSelecionarCliente(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);

        carregaDialog(parentStage, "/gui/ClienteListDialog.fxml");

    }

    public void setItemPedidoServico(ItemPedidoServico itemPedidoServico) {
        this.itemPedidoServico = itemPedidoServico;
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

        tbcProdutoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));


        tbvLocalizaProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
           if (event.getCode() == KeyCode.ENTER){
               listProdutosSelecionados.add(tbvLocalizaProduto.getSelectionModel().getSelectedItem());
               tbvLocalizaProduto.setVisible(false);
               updateFormProdutosPedido();
               txtLocalizaProduto.clear();
               txtQuantidade.clear();

           }
        });

        tbvLocalizaProduto.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2){
                Produto p = tbvLocalizaProduto.getSelectionModel().getSelectedItem();
                p.setQuantidade(Utils.converterInteiro(txtQuantidade.getText()));
                listProdutosSelecionados.add(p);
                tbvLocalizaProduto.setVisible(false);
                updateFormProdutosPedido();
                txtLocalizaProduto.clear();
                txtQuantidade.clear();
            }
        });
    }

    public void updateFormLocalizaProduto(){
        List<Produto> list = produtoServico.findAll();
        ObservableList<Produto> obbList = FXCollections.observableArrayList(list);
        filteredList = filteredTableView(obbList);

        if (filteredList.isEmpty()){
            tbvLocalizaProduto.setVisible(false);
        } else {
            tbvLocalizaProduto.setItems(filteredList);
        }
    }

    public void updateFormProdutosPedido(){
        ObservableList<Produto> obbList = FXCollections.observableArrayList(listProdutosSelecionados);
        tbvItemsPedidoPorduto.setItems(obbList);
        tbvItemsPedidoPorduto.refresh();
    }

    public synchronized void carregaDialog (Stage parentStage, String caminho){

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

        txtLocalizaProduto.textProperty().addListener(((observable, oldValue, newValue) -> this.filteredList.setPredicate(produto -> {
            if (newValue == null || newValue.isEmpty()){
                tbvLocalizaProduto.setVisible(false);
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (produto.getNome().toLowerCase().contains(lowerCaseFilter)){
                tbvLocalizaProduto.setVisible(true);
                return true;
            } else {
                return false;
            }
        })));


        return filteredList;
    }

    private ItemPedido getDataForm(){
        Pedido pedido = new Pedido();
        ItemPedido item = new ItemPedido();

        pedido.setCliente(cliente);
        pedido.setData(new Date());

        item.setQuantidade(Utils.converterInteiro(txtQuantidade.getText()));
        item.setListProduto(listProdutosSelecionados);
        item.setPedido(pedido);

        return item;
    }

    @Override
    public void onClienteChanged(Cliente cliente) {
        this.cliente = cliente;
        hyperlinkSelecionarCliente.setText(cliente.getNome());
    }
}
