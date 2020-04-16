package gui.controller;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.services.EstabelecimentoServico;
import model.services.FornecedorServico;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FornecedorCadastroController implements Initializable {

    private Fornecedor fornecedor;
    private FornecedorServico servico;
    private EstabelecimentoServico estabelecimentoServico;
    private Estabelecimento estabelecimento;

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

    public void onBtnCadastrarAction(ActionEvent event){

        try {
            fornecedor = getFormData();

            servico.saveOrUpdate(fornecedor);
            Alerts.showAlert("Fornecedor Salvo com sucesso", null, "Fornecedor "+ fornecedor.getNome() + " cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
            Utils.atualStage(event).close();

        } catch (DBException e){
            Alerts.showAlert("Erro ao cadastrar", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }


    public void onBtnCancelarAction(){

    }

    public void onBtnLimparAction(){

    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setServico(FornecedorServico servico) {
        this.servico = servico;
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes(){
        Constraints.setLabeldInteger(lblId);
    }

    public void updateFormData(){

        if (fornecedor == null){
            throw new IllegalStateException("fornecedor estava null");
        }

        lblId.setText(String.valueOf(fornecedor.getId()));
        txtNome.setText(fornecedor.getNome());

        List<Estabelecimento> estabelecimentoList = estabelecimentoServico.findAll();
        obbEstabelecimento = FXCollections.observableArrayList(estabelecimentoList);
        cbbEstabelecimento.setItems(obbEstabelecimento);

    }

    private Fornecedor getFormData() {

        Fornecedor fornecedor = new Fornecedor();

        Estabelecimento est = cbbEstabelecimento.getSelectionModel().getSelectedItem();

        fornecedor.setId(Utils.converterInteiro(lblId.getText()));
        fornecedor.setNome(txtNome.getText());
        fornecedor.setEstabelecimento(cbbEstabelecimento.getSelectionModel().getSelectedItem());

        return fornecedor;
    }

}
