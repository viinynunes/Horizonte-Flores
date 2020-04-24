package gui;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.listeners.EnderecoChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.entities.Endereco;
import model.services.ClienteServico;
import model.services.EnderecoServico;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteCadastroController implements Initializable, EnderecoChangeListener {

    private Cliente cliente;
    private Endereco endereco;
    private ClienteServico servico;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private Button btnCadastrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnLimpar;

    @FXML
    private Hyperlink hyperlinkCadEndereco;

    @FXML
    private Label lblId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtTelefone2;

    @FXML
    private TextField txtCPF;

    @FXML
    private TextField txtCNPJ;

    @FXML
    private TextArea txaEndereco;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setServico(ClienteServico servico) {
        this.servico = servico;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnCadastrarAction(ActionEvent event) {

        try {
            cliente = getFormData();
            servico.saveOrUpdate(cliente);
            Alerts.showAlert("Cliente cadastrado com sucesso", null, "Cliente " + cliente.getNome() + " cadastrado com sucesso", Alert.AlertType.CONFIRMATION);
            notifyDataChanged();
            Utils.atualStage(event).close();
        } catch (DBException e) {
            Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    @FXML
    public void onBtnCancelarAction(ActionEvent event) {
        Utils.atualStage(event).close();
    }

    @FXML
    public void onBtnLimparAction() {
        limpaForm();
    }

    @FXML
    public void onHyperlinkCadEnderecoAction(ActionEvent event) {
        Stage parentStage = Utils.atualStage(event);
        Endereco endereco = new Endereco();

        criarTelaDialog(endereco, "/gui/EnderecoCadastro.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializaNodes();
    }

    private void initializaNodes() {

        Constraints.setLabeldInteger(lblId);
    }

    public void updateFormData() {

        if (cliente == null) {
            throw new IllegalStateException("Cliente estava nulo");
        }

        lblId.setText(String.valueOf(cliente.getId()));
        txtNome.setText(cliente.getNome());
        txtEmail.setText(cliente.getEmail());
        txtTelefone.setText(cliente.getTelefone());
        txtTelefone2.setText(cliente.getTelefone2());
        txtCPF.setText(cliente.getCpf());
        txtCNPJ.setText(cliente.getCnpj());


    }

    private void setEndereco(Endereco endereco) {
        this.endereco = endereco;

        txaEndereco.setText(endereco.toString());
    }

    private Cliente getFormData() {
        Cliente cliente = new Cliente();

        cliente.setId(Utils.converterInteiro(lblId.getText()));
        cliente.setNome(txtNome.getText());
        cliente.setEmail(txtEmail.getText());
        cliente.setTelefone(txtTelefone.getText());
        cliente.setTelefone2(txtTelefone2.getText());
        cliente.setCpf(txtCPF.getText());
        cliente.setCnpj(txtCNPJ.getText());
        cliente.setEndereco(endereco);

        return cliente;
    }

    private void limpaForm() {
        txtNome.clear();
        txtEmail.clear();
        txtTelefone.clear();
        txtTelefone2.clear();
        txtCPF.clear();
        txtCNPJ.clear();
    }

    public void criarTelaDialog(Endereco endereco, String caminho, Stage parentStage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            TitledPane pane = loader.load();

            Stage dialog = new Stage();

            EnderecoCadastroController controller = loader.getController();
            controller.setEndereco(endereco);
            controller.setServico(new EnderecoServico());
            controller.subscribeEnderecoChangeListener(this);
            controller.updateDataForm();

            dialog.setTitle("Cadastro de Endereco");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Erro ao carregar tela", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onEnderecoChanged(Endereco endereco) {
        setEndereco(endereco);
    }
}
