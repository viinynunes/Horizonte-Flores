package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.ProdutoServico;

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
    private TableColumn<String, Produto> tbcFornecedorNome;

    @FXML
    private TableColumn<String, Produto> tbcCategoriaNome;

    ObservableList<Produto> obsList;

    public void setProdutoServico(ProdutoServico servico) {
        this.servico = servico;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcFornecedorNome.setCellValueFactory(new PropertyValueFactory<>("fornecedorNome"));
        tbcCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("categoriaNome"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tbvListaProduto.prefHeightProperty().bind(stage.heightProperty());
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
