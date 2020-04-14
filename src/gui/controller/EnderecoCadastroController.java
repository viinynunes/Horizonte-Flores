package gui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EnderecoCadastroController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnLimpar;
    @FXML
    private TextField txtlogadouro;
    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtBairro;
    @FXML
    private TextField txtReferencia;
    @FXML
    private TextField txtCEP;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtPais;
    @FXML
    private ComboBox<String> cbbEstado;
    ObservableList<String> obbEstado;

    public void onBtnCadastrarAction(){

    }

    public void onBtnCancelarAction(){

    }

    public void onBtnLimparAction(){


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void updateDataForm(){

    }
}
