package gui.controller;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.services.EstabelecimentoServico;

import java.net.URL;
import java.util.ResourceBundle;

public class EstabelecimentoCadastroController implements Initializable {

    private Estabelecimento estabelecimento;
    private EstabelecimentoServico servico;
    private Endereco endereco;

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

    public void onBtnCadastrarAction(ActionEvent event) {
        try {

            if (servico == null){
                throw new IllegalStateException("Servico null");
            }

            estabelecimento = getFormData();

            servico.saveOrUpdate(estabelecimento);
            Alerts.showAlert("Estabelecimento cadastrado com sucesso", null, "Estabelecimento " + estabelecimento.getNome() +
                    " cadastrado com sucesso !", Alert.AlertType.CONFIRMATION);
            Utils.atualStage(event).close();
        } catch (DBException e){
            Alerts.showAlert("Erro ao cadastrar estabelecimento", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtnCancelarAction(ActionEvent event) {
        Utils.atualStage(event).close();
    }

    public void onBtnLimparAction() {
        txtNome.clear();
        txaEndereco.clear();
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public void setServico(EstabelecimentoServico servico) {
        this.servico = servico;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitializeNodes();
    }

    private void InitializeNodes() {
        Constraints.setLabeldInteger(lblId);
    }

    public void updateFormData() {
        if (estabelecimento == null) {
            throw new IllegalStateException("Estabelecimento estava null");
        }

        lblId.setText(String.valueOf(estabelecimento.getId()));
        txtNome.setText(estabelecimento.getNome());
        txaEndereco.setText(estabelecimento.getEndereco().getLogadouro());

    }

    private Estabelecimento getFormData() {
        Estabelecimento estabelecimento = new Estabelecimento();

        estabelecimento.setId(Utils.converterInteiro(lblId.getText()));
        estabelecimento.setNome(txtNome.getText());
        estabelecimento.setEndereco(endereco);

        return estabelecimento;
    }
}
