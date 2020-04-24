package gui;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.CategoriaServico;
import model.services.EstabelecimentoServico;
import model.services.FornecedorServico;
import model.services.ProdutoServico;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ProdutoCadastroController implements Initializable, DataChangeListener {

    private Produto produto;
    private ProdutoServico produtoServico;
    private CategoriaServico categoriaServico;
    private FornecedorServico fornecedorServico;
    private EstabelecimentoServico estabelecimentoServico;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnLimpar;
    @FXML
    private Label lblId;
    @FXML
    private TextField txtNome;
    @FXML
    private Hyperlink hyperlinkCadCategoria;
    @FXML
    private Hyperlink hyperlinkCadFornecedor;

    @FXML
    private ComboBox<Categoria> cbbCategoria;
    private ObservableList<Categoria> obbListCategoria;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    private ObservableList<Fornecedor> obbFornecedor;

    @FXML
    public void onBtnCadastrarAction(ActionEvent event) {
        try {

            Produto produto = getFormData();
            produtoServico.saveOrUpdate(produto);
            Alerts.showAlert("Produto salvo com sucesso", null, "Produto " + produto.getNome() + " cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
            notifyDataChanged();
            Utils.atualStage(event).close();

        } catch (DBException e) {
            Alerts.showAlert("Erro ao cadastrar produto", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    @FXML
    public void onBtnCancelarAction(ActionEvent event) {
        Utils.atualStage(event).close();
    }

    @FXML
    public void onBtnLimparAction(){
        txtNome.clear();
    }

    @FXML
    public void onHyperlinkCadCategoriaAction(ActionEvent event) {
        Stage parentStage = Utils.atualStage(event);
        Categoria categoria = new Categoria();
        carregaDialog(parentStage, "/gui/CategoriaCadastro.fxml", (CategoriaCadastroController controller) -> {
            controller.setCategoriaServico(new CategoriaServico());
            controller.setCategoria(categoria);
            controller.subscribeDataChangeListener(this);
            controller.updateTableView();
        });
    }

    @FXML
    public void onHyperlinkCadFornecedorAction(ActionEvent event) {
        Stage parentStage = Utils.atualStage(event);
        Fornecedor fornecedor = new Fornecedor();
        carregaDialog(parentStage, "/gui/FornecedorCadastro.fxml", (FornecedorCadastroController controller) -> {
            controller.setServico(new FornecedorServico());
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.setFornecedor(fornecedor);
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
        });
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    public void setCategoriaServico(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
    }

    public void updateFormData() {

        if (produto == null) {
            throw new IllegalStateException("Produto estava vazio");
        }

        if (produto.getId() == null) {
            lblId.setText("");
        } else {
            lblId.setText(String.valueOf(produto.getId()));
        }

        txtNome.setText(produto.getNome());


        updateCbbCategoria();
        cbbCategoria.getSelectionModel().select(produto.getCategoria());
        updateCbbFornecedor();
        cbbFornecedor.getSelectionModel().select(produto.getFornecedor());
    }

    private void updateCbbCategoria(){
        List<Categoria> categoriaList = categoriaServico.findAll();

        obbListCategoria = FXCollections.observableArrayList(categoriaList);
        cbbCategoria.setItems(obbListCategoria);
    }

    private void updateCbbFornecedor(){
        List<Fornecedor> fornecedorList = fornecedorServico.findAll();
        obbFornecedor = FXCollections.observableArrayList(fornecedorList);
        cbbFornecedor.setItems(obbFornecedor);
    }

    private Produto getFormData() {

        Produto produto = new Produto();

        produto.setId(Utils.converterInteiro(lblId.getText()));
        produto.setNome(txtNome.getText());
        produto.setFornecedor(cbbFornecedor.getSelectionModel().getSelectedItem());

        produto.setCategoria(cbbCategoria.getSelectionModel().getSelectedItem());


        return produto;
    }


    public synchronized <T> void carregaDialog(Stage parentStage, String caminho, Consumer<T> init) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
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

    @Override
    public void onDataChanged() {
        updateCbbCategoria();
        updateCbbFornecedor();

    }
}