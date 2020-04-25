package gui;

import application.Main;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class ClienteListController implements Initializable, DataChangeListener {

    Stage parentStage;

    private ClienteServico servico;
    private Cliente cliente;

    FilteredList<Cliente> filteredClienteList;

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

    public void onBtnEditarAction(ActionEvent event){
        cliente = tbvListar.getSelectionModel().getSelectedItem();

        parentStage = Utils.atualStage(event);

        criarTelaDialog(cliente, "/gui/ClienteCadastro.fxml", parentStage);

    }

    public void onBtnApagarAction(){
        deleteCliente();
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

        Node node = Main.getScene().getRoot();

        node.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F2) {
                parentStage = Utils.atualStage(event);

                Cliente cliente = new Cliente();

                criarTelaDialog(cliente, "/gui/ClienteCadastro.fxml", parentStage);
            }

            if (event.getCode() == KeyCode.F3){
                cliente = tbvListar.getSelectionModel().getSelectedItem();

                parentStage = Utils.atualStage(event);

                criarTelaDialog(cliente, "/gui/ClienteCadastro.fxml", parentStage);
            }

            if (event.getCode() == KeyCode.F4){
                deleteCliente();
            }
        });

        tbvListar.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.getCode() == KeyCode.DELETE){
                deleteCliente();
            }

            if (event.getCode() == KeyCode.ENTER){
                cliente = tbvListar.getSelectionModel().getSelectedItem();

                parentStage = Utils.atualStage(event);

                criarTelaDialog(cliente, "/gui/ClienteCadastro.fxml", parentStage);
            }
        });
    }

    private void deleteCliente() {
        cliente = tbvListar.getSelectionModel().getSelectedItem();

        try {
            servico.deleteById(cliente.getId());
            updateTableView();
            Alerts.showAlert("Cliente Apagado com sucesso", null, "cliente apagado com sucesso", Alert.AlertType.CONFIRMATION);
        } catch (DBException e){
            Alerts.showAlert("Erro ao apagar cliente", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void updateTableView() {

        if (servico == null) {
            throw new IllegalStateException("Servico == null");
        }

        List<Cliente> list = servico.findAll();

        obsList = FXCollections.observableArrayList(list);

        filteredClienteList = filteredTableView(obsList);
        tbvListar.setItems(filteredClienteList);
        tbvListar.refresh();
    }

    public void criarTelaDialog(Cliente cliente, String caminho, Stage parentStage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            Pane pane = loader.load();

            Stage dialog = new Stage();

            ClienteCadastroController controller = loader.getController();
            controller.setCliente(cliente);
            controller.setServico(new ClienteServico());
            controller.subscribeDataChangeListener(this);
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

    private FilteredList<Cliente> filteredTableView(ObservableList<Cliente> obbClienteList){
        FilteredList<Cliente> filteredClienteList = new FilteredList<>(obbClienteList);

        txtProcura.textProperty().addListener(((observable, oldValue, newValue) -> {

            filteredClienteList.setPredicate(cliente -> {

                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNome().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                } else {
                    return false;
                }
            });

        }));

        return filteredClienteList;
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
