package gui.view;

import db.DBException;
import gui.listeners.EnderecoChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.services.EnderecoServico;
import model.services.EstabelecimentoServico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EstabelecimentoCadastroController implements Initializable, EnderecoChangeListener {

    private Estabelecimento estabelecimento;
    private EstabelecimentoServico servico;
    private Endereco endereco;

    @FXML
    private VBox box;
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
    @FXML
    private Hyperlink hyperlinkCadEndereco;

    public void onBtnCadastrarAction(ActionEvent event) {
        cadastrarEstabelecimento(event);
    }

    private void cadastrarEstabelecimento(Event event){

        if (txtNome.getText() == null || txtNome.getText().isEmpty()){
            Alerts.showAlert("Nome do estabelecimento vazio", null, "O nome do estabelecimento não pode estar vazio", Alert.AlertType.ERROR);
        } else if (txaEndereco.getText() == null || txaEndereco.getText().isEmpty()){
            Alerts.showAlert("Endereço do estabelecimento vazio", null, "O endereço do estabelecimento não pode estar vazio", Alert.AlertType.ERROR);
        } else {
            try {
                estabelecimento = getFormData();

                servico.saveOrUpdate(estabelecimento);
                Alerts.showAlert("Estabelecimento cadastrado com sucesso", null, "Estabelecimento " + estabelecimento.getNome() +
                        " cadastrado com sucesso !", Alert.AlertType.CONFIRMATION);
                Utils.atualStage(event).close();
            } catch (DBException e){
                Alerts.showAlert("Erro ao cadastrar estabelecimento", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void onBtnCancelarAction(ActionEvent event) {
        Utils.atualStage(event).close();
    }

    public void onBtnLimparAction() {
        limpaForm();
    }

    private void limpaForm(){
        txtNome.clear();
        txaEndereco.clear();
    }

    public void onHyperlinkCadEnderecoAction(ActionEvent event){
        Stage parentStage = Utils.atualStage(event);
        Endereco endereco = new Endereco();
        criarTelaDialog(endereco, "/gui/view/EnderecoCadastro.fxml", parentStage);
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public void setServico(EstabelecimentoServico servico) {
        this.servico = servico;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        txaEndereco.setText(endereco.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitializeNodes();

        box.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F2){
                cadastrarEstabelecimento(event);
            }

            if (event.getCode() == KeyCode.F3){
                Utils.atualStage(event).close();
            }

            if (event.getCode() == KeyCode.F4){
                limpaForm();
            }
        });
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

    }

    private Estabelecimento getFormData() {
        Estabelecimento estabelecimento = new Estabelecimento();

        estabelecimento.setId(Utils.converterInteiro(lblId.getText()));
        estabelecimento.setNome(txtNome.getText());
        estabelecimento.setEndereco(endereco);

        return estabelecimento;
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
