package gui.controller;

import com.sun.nio.sctp.IllegalReceiveException;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.listeners.EnderecoChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Endereco;
import model.services.EnderecoServico;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EnderecoCadastroController implements Initializable {

    private Endereco endereco;
    private EnderecoServico servico;
    private List<EnderecoChangeListener> enderecoChangeListeners = new ArrayList<>();

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
    private ObservableList<String> obbEstado;

    public void onBtnCadastrarAction(ActionEvent event){

        try {
            endereco = getFormData();
            servico.saveOrUpdate(endereco);
            Alerts.showAlert("Endereço cadastrado com sucesso", null, "Endereco cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
            notifyEnderecoChanged(endereco);
            Utils.atualStage(event).close();
        } catch (DBException e){
            Alerts.showAlert("Erro ao cadastrar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyEnderecoChanged(Endereco endereco) {
        for (EnderecoChangeListener listener : enderecoChangeListeners){
            listener.onEnderecoChanged(endereco);
        }
    }


    public void onBtnCancelarAction(ActionEvent event){
        Utils.atualStage(event).close();
    }

    public void onBtnLimparAction(){
        limpaForm();
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setServico(EnderecoServico servico) {
        this.servico = servico;
    }

    public void subscribeEnderecoChangeListener(EnderecoChangeListener listener){
        enderecoChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializaNodes();
    }

    private void initializaNodes(){
        Constraints.setLabeldInteger(lblId);
    }

    public void updateDataForm(){

        if (endereco == null){
            throw new IllegalStateException("endereco null");
        }

        lblId.setText(String.valueOf(endereco.getId()));
        txtlogadouro.setText(endereco.getLogadouro());
        txtNumero.setText(endereco.getNumero());
        txtBairro.setText(endereco.getBairro());
        txtReferencia.setText(endereco.getReferencia());
        txtCEP.setText(endereco.getCep());
        txtCidade.setText(endereco.getCidade());
        txtPais.setText(endereco.getPais());

        obbEstado = getEstados();
        cbbEstado.setItems(obbEstado);

    }

    private Endereco getFormData(){
        Endereco endereco = new Endereco();

        endereco.setId(Utils.converterInteiro(lblId.getText()));
        endereco.setLogadouro(txtlogadouro.getText());
        endereco.setNumero(txtNumero.getText());
        endereco.setBairro(txtBairro.getText());
        endereco.setReferencia(txtReferencia.getText());
        endereco.setCep(txtCEP.getText());
        endereco.setCidade(txtCidade.getText());
        endereco.setPais(txtPais.getText());
        endereco.setEstado(cbbEstado.getSelectionModel().getSelectedItem());

        return endereco;
    }

    private void limpaForm(){
        txtlogadouro.clear();
        txtNumero.clear();
        txtBairro.clear();
        txtReferencia.clear();
        txtCEP.clear();
        txtCidade.clear();
        txtPais.clear();
        cbbEstado.setItems(getEstados());
    }

    private ObservableList<String> getEstados (){
        ObservableList<String> obb;
        List<String> list = new ArrayList<>();

        list.add("AC");
        list.add("AL");
        list.add("AP");
        list.add("AM");
        list.add("BA");
        list.add("CE");
        list.add("DF");
        list.add("ES");
        list.add("GO");
        list.add("MA");
        list.add("MT");
        list.add("MS");
        list.add("MG");
        list.add("PA");
        list.add("PB");
        list.add("PR");
        list.add("PE");
        list.add("PI");
        list.add("RJ");
        list.add("RN");
        list.add("RS");
        list.add("RO");
        list.add("RR");
        list.add("SC");
        list.add("SP");
        list.add("SE");
        list.add("TO");

        obb = FXCollections.observableArrayList(list);

        return obb;
    }
}
