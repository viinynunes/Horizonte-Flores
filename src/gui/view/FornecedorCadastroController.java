package gui.view;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
    private FornecedorServico fornecedorServico;
    private EstabelecimentoServico estabelecimentoServico;
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

    public void onBtnCadastrarAction(ActionEvent event) {
        cadastrarFornecedor(event);
    }

    public void onBtnCancelarAction(ActionEvent event) {
        Utils.atualStage(event).close();
    }

    public void onBtnLimparAction() {
        limpaForm();
    }

    private void limpaForm() {
        txtNome.clear();
        cbbEstabelecimento.getSelectionModel().clearSelection();
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

        titledPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F2) {
                cadastrarFornecedor(event);
            }
            if (event.getCode() == KeyCode.F3) {
                Utils.atualStage(event).close();
            }
            if (event.getCode() == KeyCode.F4) {
                limpaForm();
            }
        });
    }

    private void initializeNodes() {
        Constraints.setLabeldInteger(lblId);
    }

    private void cadastrarFornecedor(Event event) {

        if (txtNome.getText() == null || txtNome.getText().isEmpty()) {
            Alerts.showAlert("Nome do fornecedor vazio", null, "O nome do fornecedor não pode estar vazio", Alert.AlertType.ERROR);
        } else if (cbbEstabelecimento.getSelectionModel().isEmpty()) {
            Alerts.showAlert("Estabelecimento não selecionado", null, "O estabelecimento do fornecedor deve ser selecionado", Alert.AlertType.ERROR);
        } else {
            try {
                fornecedor = getFormData();

                fornecedorServico.saveOrUpdate(fornecedor);
                Alerts.showAlert("Fornecedor Salvo com sucesso", null, "Fornecedor " + fornecedor.getNome() + " cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
                notifyDataChanged();
                Utils.atualStage(event).close();
            } catch (DBException e) {
                Alerts.showAlert("Erro ao cadastrar", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public void updateFormData() {

        if (fornecedorServico == null) {
            throw new IllegalStateException("fornecedor estava null");
        }

        lblId.setText(String.valueOf(fornecedor.getId()));
        txtNome.setText(fornecedor.getNome());
        cbbEstabelecimento.getSelectionModel().select(fornecedor.getEstabelecimento());

        List<Estabelecimento> estabelecimentoList = estabelecimentoServico.findAll();
        obbEstabelecimento = FXCollections.observableArrayList(estabelecimentoList);
        cbbEstabelecimento.setItems(obbEstabelecimento);

    }

    private Fornecedor getFormData() {

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setId(Utils.converterInteiro(lblId.getText()));
        fornecedor.setNome(txtNome.getText());
        fornecedor.setEstabelecimento(cbbEstabelecimento.getSelectionModel().getSelectedItem());

        return fornecedor;
    }

}
