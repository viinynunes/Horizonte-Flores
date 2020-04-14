package gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EstabelecimentoCadastroController implements Initializable {

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
    private TextArea txaEndereco;

    public void onBtnCadastrarAction(){

    }

    public void onBtnCancelarAction(){

    }

    public void onBtnLimparAction(){

        txtNome.clear();
        txaEndereco.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}