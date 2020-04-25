package gui;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TitledPane titledPane;
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
            notifyDataChanged();
            Utils.atualStage(event).close();

        } catch (DBException e){
            Alerts.showAlert("Erro ao cadastrar", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }


    public void onBtnCancelarAction(ActionEvent event){
        Utils.atualStage(event).close();
    }

    public void onBtnLimparAction(){
        limpaForm();
    }

    private void limpaForm(){
        txtNome.clear();
        cbbEstabelecimento.getSelectionModel().clearSelection();
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

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

        titledPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F2){
                try {
                    fornecedor = getFormData();

                    servico.saveOrUpdate(fornecedor);
                    Alerts.showAlert("Fornecedor Salvo com sucesso", null, "Fornecedor "+ fornecedor.getNome() + " cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
                    notifyDataChanged();
                    Utils.atualStage(event).close();

                } catch (DBException e){
                    Alerts.showAlert("Erro ao cadastrar", null, e.getMessage(), Alert.AlertType.ERROR);
                }
            }
            if (event.getCode() == KeyCode.F3){
                Utils.atualStage(event).close();
            }
            if (event.getCode() == KeyCode.F4){
                limpaForm();
            }
        });
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
