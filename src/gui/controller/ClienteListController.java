package gui.controller;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.services.ClienteServico;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteListController implements Initializable {

    private ClienteServico servico;

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
    private TableColumn<String, Cliente> tbcEndereco;

    ObservableList<Cliente> obsList;

    public void setClienteServico(ClienteServico servico) {
        this.servico = servico;
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
    }

    public void updateTableView() {

        if (servico == null) {
            throw new IllegalStateException("Servico == null");
        }

        List<Cliente> list = servico.findAll();
        obsList = FXCollections.observableArrayList(list);
        tbvListar.setItems(obsList);
    }

}
