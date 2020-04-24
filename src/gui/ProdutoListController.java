package gui;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.entities.Cliente;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.CategoriaServico;
import model.services.EstabelecimentoServico;
import model.services.FornecedorServico;
import model.services.ProdutoServico;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutoListController implements Initializable, DataChangeListener {

    private ProdutoServico servico;
    private Produto produto;
    private FilteredList<Produto> filteredProdutoList;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnApagar;

    @FXML
    private TableView<Produto> tbvListaProduto;

    @FXML
    private TableColumn<Integer, Produto> tbcId;

    @FXML
    private TableColumn<String, Produto> tbcNome;

    @FXML
    private TableColumn<Produto, Fornecedor> tbcFornecedorNome;

    @FXML
    private TableColumn<Produto, Categoria> tbcCategoriaNome;

    @FXML
    private TextField txtProcura;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane acpTitulo;

    @FXML
    private Label lblTitulo;


    ObservableList<Produto> obsList;

    @FXML
    public void onBtnNovoAction(ActionEvent event){

        Stage parentStage = Utils.atualStage(event);
        Produto produto = new Produto();
        carregaDialog(produto, "/gui/ProdutoCadastro.fxml", parentStage);

    }

    public void onBtnEditarAction(ActionEvent event){
        produto = tbvListaProduto.getSelectionModel().getSelectedItem();

        Stage parentStage = Utils.atualStage(event);

        carregaDialog(produto, "/gui/ProdutoCadastro.fxml", parentStage);
    }

    public void onBtnApagarAction(){
        produto = tbvListaProduto.getSelectionModel().getSelectedItem();

        try {
            servico.deleteById(produto.getId());
            updateTableView();
            Alerts.showAlert("Produto Apagado com sucesso", null, "Produto apagado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (DBException e){
            Alerts.showAlert("Erro ao apagar produto", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setProdutoServico(ProdutoServico servico) {
        this.servico = servico;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcFornecedorNome.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        tbcCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tbvListaProduto.prefHeightProperty().bind(stage.heightProperty());

        Node node = Main.getScene().getRoot();

        node.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.F2){
                    Stage parentStage = Utils.atualStage(event);
                    Produto produto = new Produto();
                    carregaDialog(produto, "/gui/ProdutoCadastro.fxml", parentStage);
                }
            }
        });
    }

    public void updateTableView() {
        if (servico == null) {
            throw new IllegalStateException("Servico esta nullo, nao implementado");
        }

        List<Produto> list = servico.findAll();
        obsList = FXCollections.observableArrayList(list);

        filteredProdutoList = filteredTableView(obsList);
        tbvListaProduto.setItems(filteredProdutoList);
        tbvListaProduto.refresh();
        tbvListaProduto.getSelectionModel().selectFirst();
    }

    public void carregaDialog(Produto produto, String caminho, Stage parentStage){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Pane pane = loader.load();

            Stage dialog = new Stage();

            ProdutoCadastroController controller = loader.getController();
            controller.setProduto(produto);
            controller.setProdutoServico(new ProdutoServico());
            controller.setCategoriaServico(new CategoriaServico());
            controller.setFornecedorServico(new FornecedorServico());
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            dialog.setTitle("Cadastro de Produto");
            dialog.setResizable(false);
            dialog.setScene(new Scene(pane));
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();

        } catch (IOException e){
            Alerts.showAlert("Erro ao carregar a tela", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private FilteredList<Produto> filteredTableView(ObservableList<Produto> obbProdutoList){
        FilteredList<Produto> filteredClienteList = new FilteredList<>(obbProdutoList);

        txtProcura.textProperty().addListener(((observable, oldValue, newValue) -> {

            filteredProdutoList.setPredicate(produto -> {

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (produto.getNome().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                } else {
                    return false;
                }
            });

        }));

        return filteredClienteList;
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}