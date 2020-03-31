package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Cliente;

import java.net.URL;
import java.util.ResourceBundle;

public class ClienteListController implements Initializable {

    @FXML private Button btnNovo;
    @FXML private Button btnEditar;
    @FXML private Button btnApagar;
    @FXML private TableView<Cliente> tbvListar;
    @FXML private TableColumn<String, Cliente> tbcId;
    @FXML private TableColumn<String, Cliente> tbcNome;
    @FXML private TableColumn<String, Cliente> tbcTelefone;
    @FXML private TableColumn<String, Cliente> tbcTelefone2;
    @FXML private TableColumn<String, Cliente> tbcEmail;
    @FXML private TableColumn<String, Cliente> tbcCPF;
    @FXML private TableColumn<String, Cliente> tbcCNPJ;
    @FXML private TableColumn<String, Cliente> tbcEndereco;


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

    }


}
