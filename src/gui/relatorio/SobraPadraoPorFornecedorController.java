package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.entities.*;
import model.services.FornecedorServico;
import model.services.ProdutoServico;
import model.services.SobraProdutoPadraoServico;
import model.services.SobraServico;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SobraPadraoPorFornecedorController implements Initializable {

    private FornecedorServico fornecedorServico;
    private ProdutoServico produtoServico;
    private SobraProdutoPadraoServico sobraProdutoPadraoServico;
    private Fornecedor fornecedor;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    @FXML
    private Button btnGerarProdutos;
    @FXML
    private TableView<Produto> tbvProduto;
    @FXML
    private TableColumn<String, Produto> tbcSelecionaProduto;
    @FXML
    private TableColumn<Integer, Produto> tbcProdutoId;
    @FXML
    private TableColumn<String, Produto> tbcProdutoNome;
    @FXML
    private TableColumn<Produto, Categoria> tbcProdutoCategoria;
    @FXML
    private ListView<SobraProdutoPadrao> lvProduto;

    public void onBtnGerarProdutosAction() {
        fornecedor = cbbFornecedor.getSelectionModel().getSelectedItem();

        try {
            ObservableList<Produto> obbListProduto = FXCollections.observableArrayList(
                    produtoServico.findByFornecedor(fornecedor));

            tbvProduto.setItems(obbListProduto);

            updateListViewForData();

        } catch (DBException e) {
            e.printStackTrace();
        }

    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public void setSobraProdutoPadraoServico(SobraProdutoPadraoServico sobraProdutoPadraoServico) {
        this.sobraProdutoPadraoServico = sobraProdutoPadraoServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbcProdutoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcProdutoCategoria.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCategoria().getNome()));

        tbvProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                addProdutoSobraPadrao();
            }
        });

        tbvProduto.setOnMousePressed(event ->{
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2){
                addProdutoSobraPadrao();
            }
        });

        lvProduto.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                removeProdutoSobraPadrao();
            }
        });

        lvProduto.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2){
                removeProdutoSobraPadrao();
            }
        });
    }

    private void addProdutoSobraPadrao(){
        Produto p = tbvProduto.getSelectionModel().getSelectedItem();
        SobraProdutoPadrao padrao = new SobraProdutoPadrao();
        padrao.setProduto(p);

        sobraProdutoPadraoServico.insertOrUpdate(padrao);
        updateListViewForData();
    }

    private void removeProdutoSobraPadrao(){
        SobraProdutoPadrao sobra = lvProduto.getSelectionModel().getSelectedItem();

        if (sobra == null){
              Alerts.showAlert("Erro", null, "Selecione um produto", Alert.AlertType.ERROR);
        } else {
            try {
                sobraProdutoPadraoServico.deleteById(sobra.getId());
                updateListViewForData();
            }catch (DBException e){
                Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }


    public void updateCbbFornecedorFormData() {
        if (fornecedorServico == null) {
            throw new IllegalStateException("Fornecedor Servico null");
        }

        ObservableList<Fornecedor> obbList = FXCollections.observableArrayList(fornecedorServico.findAll());
        cbbFornecedor.setItems(obbList);
        cbbFornecedor.getSelectionModel().select(0);
    }

    private void updateListViewForData(){
        if (sobraProdutoPadraoServico == null){
            throw new IllegalStateException("Sobra padrao Servico null");
        }

        ObservableList<SobraProdutoPadrao> obbList = FXCollections.observableArrayList(
                sobraProdutoPadraoServico.findByFornecedor(fornecedor));

        lvProduto.setItems(obbList);
        lvProduto.refresh();
    }
}
