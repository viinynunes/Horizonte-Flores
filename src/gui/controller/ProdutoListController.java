package gui.controller;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.ProdutoServico;

import java.awt.event.KeyListener;
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


    private TextField txtProcura;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane acpTitulo;

    @FXML
    private Label lblTitulo;


    ObservableList<Produto> obsList;

    public void onBtnNovoAction(){
        System.out.println("Novo Produto");
    }

    public void onBtnEditarAction(){
        System.out.println("Editar Produto");
    }

    public void onBtnApagarAction(){
        System.out.println("Apagar Produto");
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

        stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {

                if (ke.getCode() == KeyCode.F2){
                    System.out.println("F2");
                    onBtnNovoAction();
                }
                if (ke.getCode() == KeyCode.F3){
                    System.out.println("F3");
                    onBtnEditarAction();
                }
                if (ke.getCode() == KeyCode.F4){
                    System.out.println("F4");
                    onBtnApagarAction();
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
        tbvListaProduto.setItems(obsList);
    }
}
