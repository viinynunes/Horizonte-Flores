package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import gui.util.ExportExcel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.entities.*;
import model.services.*;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioGeralEstabelecimentoController implements Initializable {

    private EstabelecimentoServico estabelecimentoServico;
    private SobraServico sobraServico;
    private FilteredList<Produto> filteredList;
    private ProdutoServico produtoServico;
    private ItemPedidoServico itemPedidoServico;
    private java.sql.Date iniDate, endDate;
    private Estabelecimento estabelecimento;
    private List<Sobra> sobraList;

    @FXML
    private DatePicker dpInicial = new DatePicker();
    @FXML
    private DatePicker dpFinal = new DatePicker();
    @FXML
    private ComboBox<Estabelecimento> cbbEstabelecimento;
    @FXML
    private Button btnGerarRelatorio;
    @FXML
    private Button btnExportar;
    @FXML
    private TableView<Sobra> tbvRelatorio;
    @FXML
    private TableColumn<Sobra, Fornecedor> tbcNomeFornecedor;
    @FXML
    private TableColumn<Sobra, Produto> tbcNomeProduto;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalPedido;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalPedidoAtualizado;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalSobra;
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

    @FXML
    public void onBtnGerarRelatorioAction() {
        iniDate = Date.valueOf(dpInicial.getValue());
        endDate = Date.valueOf(dpFinal.getValue());
        estabelecimento = cbbEstabelecimento.getSelectionModel().getSelectedItem();

        try {

            sobraList = sobraServico.findByEstabelecimento(estabelecimento, iniDate, endDate);
            ObservableList<Sobra> obbList = FXCollections.observableArrayList(sobraList);
            tbvRelatorio.setItems(obbList);

        } catch (DBException e){
            Alerts.showAlert("Erro ao listar o relatório", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtnExportarAction(){
        Sobra sobra = sobraList.get(0);
        String nome = sobra.getProduto().getFornecedor().getEstabelecimento().getNome() + " " + sobra.getData().toString();
        ExportExcel.createExcelByEstabelecimento(sobraList, nome);
    }

    @FXML
    public void onCbbEstabelecimentoAction() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcNomeFornecedor.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getFornecedor().getNome()));
        tbcNomeProduto.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcTotalPedido.setCellValueFactory(new PropertyValueFactory<>("totalPedido"));
        tbcTotalPedidoAtualizado.setCellValueFactory(new PropertyValueFactory<>("totalPedidoAtualizado"));
        tbcTotalSobra.setCellValueFactory(new PropertyValueFactory<>("sobra"));

        tbcProdutoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcProdutoFornecedor.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFornecedor().getNome()));

        tbcPedidoId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getPedido().getId()));
        tbcPedidoCliente.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getPedido().getCliente().getNome()));
        tbcPedidoProduto.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcPedidoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        dpInicial.setValue(LocalDate.now());
        dpFinal.setValue(LocalDate.now());

        tbcPedidoData.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getPedido().getData()));

        tbvListaProduto.addEventFilter(KeyEvent.KEY_PRESSED, getKeyEventHandler());

        txtLocalizaProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                selecionaPrimeiroProduto();
            }
        });

        tbvListaProduto.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2){
                selecionaProdutoSelecionado();
            }
        });
        txtLocalizaProduto.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2){
                selecionaProdutoSelecionado();
            }
        });
    }

    public void setSobraServico(SobraServico sobraServico) {
        this.sobraServico = sobraServico;
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
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

    public void updateFormData(){
        if (estabelecimentoServico == null){
            throw new IllegalStateException("Estabelecimento Servico null");
        }

        ObservableList<Estabelecimento> obbList = FXCollections.observableArrayList(estabelecimentoServico.findAll());
        cbbEstabelecimento.setItems(obbList);
        cbbEstabelecimento.getSelectionModel().select(0);
    }

    private void selecionaPrimeiroProduto(){
        Produto produto = tbvListaProduto.getItems().get(0);
        tbvListaProduto.setVisible(false);
        updatePedidoFormData(produto);
    }

    private void selecionaProdutoSelecionado(){
        Produto produto = tbvListaProduto.getSelectionModel().getSelectedItem();
        updatePedidoFormData(produto);
        tbvListaProduto.setVisible(false);
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

        return (EventHandler<KeyEvent>) event -> {
            if (event.getCode() == KeyCode.ENTER) {
                selecionaProdutoSelecionado();
            }
        };
    }
}

