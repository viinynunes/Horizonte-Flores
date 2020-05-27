package gui.view;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.entities.Fornecedor;
import model.entities.ItemPedido;
import model.entities.Pedido;
import model.entities.Produto;
import model.services.ItemPedidoServico;
import model.services.ProdutoServico;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransacoesProdutoController implements Initializable {

    private FilteredList<Produto> filteredList;
    private ProdutoServico produtoServico;
    private ItemPedidoServico itemPedidoServico;

    @FXML
    private TextField txtLocalizaProduto;
    @FXML
    private TableView<Produto> tbvListaProduto;
    @FXML
    private TableView<ItemPedido> tbvListaPedido;
    @FXML
    private TableColumn<Integer, Produto> tbcProdutoId;
    @FXML
    private TableColumn<String, Produto> tbcProdutoNome;
    @FXML
    private TableColumn<Produto, Fornecedor> tbcProdutoFornecedor;
    @FXML
    private TableColumn<ItemPedido, Pedido> tbcPedidoId;
    @FXML
    private TableColumn<ItemPedido, Pedido> tbcPedidoData;
    @FXML
    private TableColumn<ItemPedido, Pedido> tbcPedidoCliente;
    @FXML
    private TableColumn<ItemPedido, Produto> tbcPedidoProduto;
    @FXML
    private TableColumn<Integer, ItemPedido> tbcPedidoQuantidade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbcProdutoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcProdutoFornecedor.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFornecedor().getNome()));

        tbcPedidoId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getPedido().getId()));
        tbcPedidoCliente.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getPedido().getCliente().getNome()));
        tbcPedidoProduto.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcPedidoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        tbcPedidoData.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getPedido().getData()));

        tbvListaProduto.addEventFilter(KeyEvent.KEY_PRESSED, getKeyEventHandler());

        txtLocalizaProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                Produto produto = tbvListaProduto.getItems().get(0);
                tbvListaProduto.setVisible(false);
                updatePedidoFormData(produto);
            }
        });
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public void setItemPedidoServico(ItemPedidoServico itemPedidoServico) {
        this.itemPedidoServico = itemPedidoServico;
    }

    private void updatePedidoFormData(Produto produto){
        if (itemPedidoServico == null){
            throw new IllegalStateException("item servico null");
        }

        List<ItemPedido> itemPedidoList = itemPedidoServico.findByProduto(produto);
        ObservableList<ItemPedido> obbList = FXCollections.observableArrayList(itemPedidoList);
        tbvListaPedido.setItems(obbList);
    }

    public void updateLocalizaProdutoFormData(){

        List<Produto> list = produtoServico.findAll();
        ObservableList<Produto> obbList = FXCollections.observableArrayList(list);
        filteredList = filteredTableView(obbList);
        tbvListaProduto.setItems(filteredList);
    }


    private FilteredList<Produto> filteredTableView(ObservableList<Produto> obbProdutoList) {
        FilteredList<Produto> filteredList = new FilteredList<>(obbProdutoList);

        txtLocalizaProduto.textProperty().addListener(((observable, oldValue, newValue) -> this.filteredList.setPredicate(produto -> {
            if (newValue == null || newValue.isEmpty()) {
                tbvListaProduto.setVisible(false);
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (produto.getNome().toLowerCase().contains(lowerCaseFilter)) {
                tbvListaProduto.setVisible(true);
                return true;
            } else if (produto.getId().toString().contains(lowerCaseFilter)) {
                tbvListaProduto.setVisible(true);
                return true;
            } else if (produto.getFornecedor().getNome().toLowerCase().contains(lowerCaseFilter)) {
                tbvListaProduto.setVisible(true);
                return true;
            } else {
                return false;
            }
        })));

        return filteredList;
    }

    private EventHandler getKeyEventHandler(){
        EventHandler<KeyEvent> keyEventEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    Produto produto = tbvListaProduto.getSelectionModel().getSelectedItem();
                    updatePedidoFormData(produto);
                    tbvListaProduto.setVisible(false);
                }
            }
        };

        return keyEventEventHandler;
    }

}
