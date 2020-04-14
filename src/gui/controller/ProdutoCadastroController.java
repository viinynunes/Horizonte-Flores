package gui.controller;

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
import model.services.EstabelecimentoServico;
import model.services.FornecedorServico;
import model.services.ProdutoServico;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutoCadastroController implements Initializable {

    private Produto produto;
    private ProdutoServico produtoServico;
    private CategoriaServico categoriaServico;
    private FornecedorServico fornecedorServico;
    private EstabelecimentoServico estabelecimentoServico;

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
    private ObservableList<Fornecedor> obbFornecedor;

    @FXML
    private ComboBox<Estabelecimento> cbbEstabelecimento;
    private ObservableList<Estabelecimento> obbEstabelecimento;

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

    public void setFornecedorServico(FornecedorServico fornecedorServico){ this.fornecedorServico = fornecedorServico;}

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico){
        this.estabelecimentoServico = estabelecimentoServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {

        lblId.setText(" ");
    }

    public void updateFormData() {

        if (produto == null) {
            throw new IllegalStateException("Produto estava vazio");
        }

        if (produto.getId() == null){
            lblId.setText("");
        } else {
            lblId.setText(String.valueOf(produto.getId()));
        }

        txtNome.setText(produto.getNome());

        List<Categoria> categoriaList = categoriaServico.findAll();

        obbListCategoria = FXCollections.observableArrayList(categoriaList);
        cbbCategoria.setItems(obbListCategoria);

        List<Fornecedor> fornecedorList = fornecedorServico.findAll();
        obbFornecedor = FXCollections.observableArrayList(fornecedorList);
        cbbFornecedor.setItems(obbFornecedor);

        List<Estabelecimento> estabelecimentoList = estabelecimentoServico.findAll();
        obbEstabelecimento = FXCollections.observableArrayList(estabelecimentoList);
        cbbEstabelecimento.setItems(obbEstabelecimento);
    }
}
