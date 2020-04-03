package gui.controller;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.ProdutoServico;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutoListController implements Initializable {

    private ProdutoServico servico;

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

   /* @FXML
    private TextField txtProcura;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane acpTitulo;

    @FXML
    private Label lblTitulo;


    */

    ObservableList<Produto> obsList;

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
        //acpTitulo.prefWidthProperty().bind(stage.widthProperty());
        //lblTitulo.prefWidthProperty().bind(stage.widthProperty());

        // anchorPane.prefWidthProperty().bind(stage.widthProperty());

        //txtProcura.prefWidthProperty().bind(anchorPane.widthProperty());
    }

    public void updateTableView() {
        if (servico == null) {
            throw new IllegalStateException("Servico esta nullo, nao implementado");
        }

        List<Produto> list = servico.findAll();
        obsList = FXCollections.observableArrayList(list);
        tbvListaProduto.setItems(obsList);
    }
}
