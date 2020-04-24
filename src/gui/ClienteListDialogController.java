package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.entities.Cliente;
import model.services.ClienteServico;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteListDialogController implements Initializable {

    private ClienteServico servico;

    @FXML
    private TextField txtProcura;
    @FXML
    private Button btnNovo;
    @FXML
    private TableView<Cliente> tbvListaCliente;
    @FXML
    private TableColumn<String, Cliente> tbcClienteId;
    @FXML
    private TableColumn<String, Cliente> tbcClienteNome;
    @FXML
    private TableColumn<String, Cliente> tbcClienteTelefone;

    private ObservableList<Cliente> obbList;

    public void onBtnNovoAction(ActionEvent event) {

    }

    public void setServico(ClienteServico servico) {
        this.servico = servico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcClienteTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
    }

    public void updateDataForm() {

        if (servico == null) {
            throw new IllegalStateException("servico estava null");
        }

        List<Cliente> list = servico.findAll();
        obbList = FXCollections.observableArrayList(list);
        tbvListaCliente.setItems(obbList);
    }
}
