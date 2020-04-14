package gui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Estabelecimento;

import java.net.URL;
import java.util.ResourceBundle;

public class FornecedorCadastroController implements Initializable {

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
    private ComboBox<Estabelecimento> cbbEstabelecimento;
    private ObservableList<Estabelecimento> obbEstabelecimento;

    public void onBtnCadastrarAction(){

    }

    public void onBtnCancelarAction(){

    }

    public void onBtnLimparAction(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
