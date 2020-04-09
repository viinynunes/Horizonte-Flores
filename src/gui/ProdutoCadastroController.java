package gui;

import gui.util.Constraints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Categoria;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.services.CategoriaServico;
import model.services.ProdutoServico;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutoCadastroController implements Initializable {

    private Produto produto;
    private ProdutoServico produtoServico;
    private CategoriaServico categoriaServico;

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
    private ComboBox<Categoria> cbbCategoria;

    private ObservableList<Categoria> obbListCategoria;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    @FXML
    private ComboBox<Estabelecimento> cbbEstabelecimento;

    @FXML
    public void onBtnCadastrarAction() {

    }

    @FXML
    public void onBtnCancelarAction() {

    }

    @FXML
    public void onBtnLimparAction() {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        //initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setLabeldInteger(lblId);
    }

    public void updateFormData() {

        if (produto == null) {
            throw new IllegalStateException("Produto estava vazio");
        }

        lblId.setText(String.valueOf(produto.getId()));
        txtNome.setText(produto.getNome());

        List<Categoria> list = categoriaServico.findAll();

        obbListCategoria = FXCollections.observableArrayList(list);
        cbbCategoria.setItems(obbListCategoria);
    }
}
