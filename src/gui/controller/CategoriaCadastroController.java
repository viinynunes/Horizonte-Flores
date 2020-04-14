package gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Categoria;
import model.services.CategoriaServico;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriaCadastroController implements Initializable {

    private CategoriaServico servico;
    private Categoria categoria;

    @FXML
    private Label lblId;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnLimpar;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtAbreviacao;

    public void onBtnCadastrarAction() {

    }

    public void onBtnCancelarAction() {

    }

    public void onBtnLimparAction() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setCategoriaServico(CategoriaServico servico) {
        this.servico = servico;
    }

    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    public void updateTableView() {

        if (categoria == null){
            throw  new IllegalStateException("Categoria nao instanciada");
        }

        lblId.setText(String.valueOf(categoria.getId()));
        txtNome.setText(categoria.getNome());
        txtAbreviacao.setText(categoria.getAbreviacao());
    }
}
