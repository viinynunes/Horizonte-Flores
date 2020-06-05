package gui.view;

import application.Main;
import db.DBException;
import gui.listeners.ClienteChangeListener;
import gui.listeners.DataChangeListener;
import gui.listeners.PedidoChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.*;
import model.services.*;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PedidoCadastroController implements Initializable, ClienteChangeListener, DataChangeListener {

    private PedidoServico pedidoServico;
    private ProdutoServico produtoServico;
    private Pedido pedido;
    private FilteredList<Produto> filteredList;
    private List<ItemPedido> itemPedidoList;
    private Cliente cliente;
    private List<PedidoChangeListener> pedidoChangeListeners = new ArrayList<>();
    private int quantidadeItens, quantidadePadrao = 1;

    @FXML
    private VBox vBox;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnApagarItem;
    @FXML
    private Button btnNovoProduto;
    @FXML
    private TextField txtQuantidadePadrao;
    @FXML
    private Hyperlink hyperlinkSelecionarCliente;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtLocalizaProduto;
    @FXML
    private Label lblQuantidadeItens;
    @FXML
    private TableView<ItemPedido> tbvItemsPedidoPorduto;
    @FXML
    private TableColumn<ItemPedido, Produto> tbcProdutoId;
    @FXML
    private TableColumn<ItemPedido, Produto> tbcProdutoNome;
    @FXML
    private TableColumn<Integer, ItemPedido> tbcQuantidade;
    @FXML
    private TableView<Produto> tbvLocalizaProduto;
    @FXML
    private TableColumn<String, Produto> tbcLocalizaProdutoNome;
    @FXML
    private TableColumn<Produto, Categoria  > tbcLocalizaProdutoCategoria;
    @FXML
    private TableColumn<Produto, Fornecedor> tbcLocalizaProdutoFornecedor;

    public void onBtnSalvarAction(ActionEvent event) {
        salvarPedido(event);
    }

    public void onBtnCancelarAction(ActionEvent event) {
        cancelarPedido(event);
    }

    public void onBtnApagarItemAction() {
        apagarItem();
    }

    public void onHyperlinkSelecionarCliente(ActionEvent event) {
        selecionarCliente(event);
    }

    public void onBtnNovoProdutoAction(Event event){
        cadastrarProduto(event);
    }

    public void onTxtQuantidadePadraoAction(){
        quantidadePadrao = Integer.parseInt(txtQuantidadePadrao.getText());
        txtQuantidade.setText(String.valueOf(quantidadePadrao));
        txtQuantidade.requestFocus();
    }

    private void salvarPedido(Event event) {
        try {
            pedido = getDataForm();

            if (pedido.getCliente() == null) {
                Alerts.showAlert("Cliente não selecionado", null, "Selecione um cliente", Alert.AlertType.INFORMATION);

            } else if (pedido.getItemPedidoList().isEmpty() || pedido.getItemPedidoList() == null) {
                Alerts.showAlert("Nenhum produto selecionado", null, "Nenhum produto no pedido", Alert.AlertType.INFORMATION);
            } else {
                pedidoServico.saveOrUpdate(pedido);
                notifyDataChanged();
                itemPedidoList.clear();
                Utils.atualStage(event).close();
            }

        } catch (DBException e) {
            Alerts.showAlert("Erro ao salvar pedido", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cancelarPedido(Event event) {
        itemPedidoList.clear();
        Utils.atualStage(event).close();
    }

    private void apagarItem() {
        itemPedidoList.remove(tbvItemsPedidoPorduto.getSelectionModel().getSelectedItem());
        updateFormProdutosPedido();
        txtQuantidade.requestFocus();
        txtQuantidade.setText(String.valueOf(1));
    }

    private void selecionarCliente(Event event) {
        Stage parentStage = Utils.atualStage(event);
        carregaDialog(parentStage, "/gui/view/ClienteListDialog.fxml", (ClienteListDialogController controller) ->{
            controller.setServico(new ClienteServico());
            controller.subscribeDataChangeListener(this);
            controller.updateDataForm();
        });
        txtQuantidade.requestFocus();
    }

    private void cadastrarProduto(Event event){
        Stage parentStage = Utils.atualStage(event);
        Produto produto = new Produto();
        carregaDialog(parentStage, "/gui/view/ProdutoCadastro.fxml", (ProdutoCadastroController controller) -> {
            controller.setProduto(produto);
            controller.setProdutoServico(new ProdutoServico());
            controller.setFornecedorServico(new FornecedorServico());
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.setCategoriaServico(new CategoriaServico());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
        });
    }

    private void notifyDataChanged() {
        for (PedidoChangeListener listener : pedidoChangeListeners) {
            listener.onDataChangedListener(pedido);
        }
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setPedidoServico(PedidoServico pedidoServico) {
        this.pedidoServico = pedidoServico;
    }

    public void setItemPedidoList(List<ItemPedido> itemPedidoList) {
        this.itemPedidoList = itemPedidoList;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public void subscribeDataChangeListener(PedidoChangeListener listener) {
        pedidoChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Stage stage = (Stage) Main.getScene().getWindow();
        tbvItemsPedidoPorduto.prefHeightProperty().bind(stage.heightProperty());

        //tabela da busca de produtos
        tbcLocalizaProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcLocalizaProdutoCategoria.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCategoria().getNome()));
        tbcLocalizaProdutoFornecedor.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFornecedor().getNome()));

        //tabela de itens do pedido selecionados
        tbcProdutoId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getId()));
        tbcProdutoNome.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        txtQuantidade.setText(String.valueOf(1));


        vBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F2) {
                salvarPedido(event);
            }
            if (event.getCode() == KeyCode.F3) {
                cancelarPedido(event);
            }
            if (event.getCode() == KeyCode.F4) {
                apagarItem();
            }
            if (event.getCode() == KeyCode.F5) {
                selecionarCliente(event);
            }
            if (event.getCode() == KeyCode.F10){
                cadastrarProduto(event);
            }
        });

        tbvLocalizaProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Produto p = tbvLocalizaProduto.getSelectionModel().getSelectedItem();
                addItemPedido(p);
            }
        });


        tbvLocalizaProduto.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Produto p = tbvLocalizaProduto.getSelectionModel().getSelectedItem();
                addItemPedido(p);
            }
        });

        txtLocalizaProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Produto p = tbvLocalizaProduto.getItems().get(0);
                addItemPedido(p);
            }
            if (event.getCode() == KeyCode.TAB){
                tbvItemsPedidoPorduto.setFocusTraversable(false);
                tbvLocalizaProduto.requestFocus();
            }
            if (event.getCode() == KeyCode.BACK_SPACE){
                if (txtLocalizaProduto.getText().isEmpty()){
                    txtQuantidade.requestFocus();
                }
            }
        });
    }

    private void addItemPedido(Produto produto) {

        if (txtQuantidade.getText().isEmpty() || txtQuantidade.getText() == null) {
            Alerts.showAlert("Digite a quantidade do produto", null, "Digite a quantidade do produto", Alert.AlertType.INFORMATION);
        } else if (produto == null) {
            Alerts.showAlert("Selecione um produto", null, "Selecione um produto", Alert.AlertType.INFORMATION);
        } else {
            ItemPedido item = new ItemPedido();
            item.setQuantidade(Utils.converterInteiro(txtQuantidade.getText()));
            item.setProduto(produto);
            itemPedidoList.add(item);
            tbvLocalizaProduto.setVisible(false);
            updateFormProdutosPedido();
            txtLocalizaProduto.clear();
            txtQuantidade.clear();
            txtQuantidade.setText(String.valueOf(quantidadePadrao));
            quantidadeItens = itemPedidoList.size();
            lblQuantidadeItens.setText(String.valueOf(quantidadeItens));
            txtQuantidade.requestFocus();

        }
    }

    public void updateFormLocalizaProduto() {
        List<Produto> list = produtoServico.findAll();
        ObservableList<Produto> obbList = FXCollections.observableArrayList(list);
        filteredList = filteredTableView(obbList);

        if (filteredList.isEmpty()) {
            tbvLocalizaProduto.setVisible(false);
        } else {
            tbvLocalizaProduto.setItems(filteredList);
        }
    }

    public void updateFormProdutosPedido() {

        if (pedido == null) {
            throw new IllegalStateException("pedido null");
        }

        if (itemPedidoList == null) {
            throw new IllegalStateException("lista de itens null");
        }

        ObservableList<ItemPedido> obbList;

        if (pedido.getCliente() != null) {
            hyperlinkSelecionarCliente.setText(pedido.getCliente().toString());
        }

        obbList = FXCollections.observableArrayList(itemPedidoList);
        tbvItemsPedidoPorduto.setItems(obbList);
        tbvItemsPedidoPorduto.refresh();
        quantidadeItens = obbList.size();

        lblQuantidadeItens.setText(String.valueOf(quantidadeItens));
    }

    public synchronized <T> void carregaDialog(Stage parentStage, String caminho, Consumer<T> initConsumer) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            VBox vBox = loader.load();

            Stage dialog = new Stage();

            T controller = loader.getController();
            initConsumer.accept(controller);

            dialog.setScene(new Scene(vBox));
            dialog.initOwner(parentStage);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FilteredList<Produto> filteredTableView(ObservableList<Produto> obbProdutoList) {
        FilteredList<Produto> filteredList = new FilteredList<>(obbProdutoList);

        txtLocalizaProduto.textProperty().addListener(((observable, oldValue, newValue) -> this.filteredList.setPredicate(produto -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (produto.getNome().toLowerCase().contains(lowerCaseFilter)) {
                tbvLocalizaProduto.setVisible(true);
                return true;
            } else if (produto.getId().toString().contains(lowerCaseFilter)) {
                tbvLocalizaProduto.setVisible(true);
                return true;
            } else if (produto.getFornecedor().getNome().toLowerCase().contains(lowerCaseFilter)) {
                tbvLocalizaProduto.setVisible(true);
                return true;
            } else {
                return false;
            }
        })));

        return filteredList;
    }

    private Pedido getDataForm() {

            if (pedido.getId() == null) {
                Pedido pedidoNovo = new Pedido();
                pedidoNovo.setCliente(cliente);
                pedidoNovo.setData(new Date());
                pedidoNovo.setItemPedidoList(itemPedidoList);

                return pedidoNovo;
            } else {
                pedido.setCliente(pedido.getCliente());
                pedido.setData(pedido.getData());
                pedido.setItemPedidoList(itemPedidoList);

                return pedido;
            }
    }

    @Override
    public void onClienteChanged(Cliente cliente) {
        this.cliente = cliente;
        hyperlinkSelecionarCliente.setText(cliente.getNome());
    }

    @Override
    public void onDataChanged() {
        updateFormLocalizaProduto();
    }
}
