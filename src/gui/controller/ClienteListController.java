package gui.controller;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.entities.Endereco;
import model.services.ClienteServico;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteListController implements Initializable {

    Stage parentStage;

    private ClienteServico servico;

    private Cliente cliente;

    @FXML
    private VBox vBox;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnApagar;
    @FXML
    private TableView<Cliente> tbvListar;
    @FXML
    private TableColumn<Integer, Cliente> tbcId;
    @FXML
    private TableColumn<String, Cliente> tbcNome;
    @FXML
    private TableColumn<String, Cliente> tbcTelefone;
    @FXML
    private TableColumn<String, Cliente> tbcTelefone2;
    @FXML
    private TableColumn<String, Cliente> tbcEmail;
    @FXML
    private TableColumn<String, Cliente> tbcCPF;
    @FXML
    private TableColumn<String, Cliente> tbcCNPJ;
    @FXML
    private TableColumn<Cliente, Endereco> tbcEndereco;
    @FXML
    private TextField txtProcura;

    ObservableList<Cliente> obsList;

    public void onBtnNovoAction(ActionEvent event) {

        parentStage = Utils.atualStage(event);

        Cliente cliente = new Cliente();

        criarTelaDialog(cliente, "/gui/ClienteCadastro.fxml", parentStage);
    }



    public void setClienteServico(ClienteServico servico) {
        this.servico = servico;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void initialize(URL url, ResourceBundle rs) {

        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        tbcTelefone2.setCellValueFactory(new PropertyValueFactory<>("telefone2"));
        tbcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbcCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tbcCNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        tbcEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        Stage stage = (Stage) Main.getScene().getWindow();
        tbvListar.prefHeightProperty().bind(stage.heightProperty());
/*
        stage.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {

            if (ke.getCode() == KeyCode.F2){

            }

        });e
 */

    }

    public void updateTableView() {

        if (servico == null) {
            throw new IllegalStateException("Servico == null");
        }

        List<Cliente> list = servico.findAll();
        obsList = FXCollections.observableArrayList(list);
        tbvListar.setItems(obsList);
    }

    public void criarTelaDialog(Cliente cliente, String caminho, Stage parentStage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Pane pane = loader.load();

            Stage dialog = new Stage();

            ClienteCadastroController controller = loader.getController();
            controller.setCliente(cliente);
            controller.setServico(new ClienteServico());
            controller.updateFormData();

            dialog.setTitle("Cadastro de Cliente");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Erro ao carregar tela", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }



}
