package gui;

import gui.listeners.ClienteChangeListener;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import model.entities.Cliente;
import model.services.ClienteServico;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClienteListDialogController implements Initializable {

    private ClienteServico servico;
    private Cliente cliente;
    private FilteredList<Cliente> filterdClienteList;
    private List<ClienteChangeListener> clienteChangeListeners = new ArrayList<>();

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

    public void setCliente(Cliente cliente) {this.cliente = cliente; }

    public void subscribeDataChangeListener(ClienteChangeListener listener){
        clienteChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcClienteTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        tbvListaCliente.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setCliente(tbvListaCliente.getSelectionModel().getSelectedItem());
                notifyDataChanged();
                Utils.atualStage(event).close();
            }

            if (event.getCode() == KeyCode.F2){
                //carrega dialog cadastro de cliente
            }
        });

        tbvListaCliente.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                setCliente(tbvListaCliente.getSelectionModel().getSelectedItem());
                notifyDataChanged();
                Utils.atualStage(event).close();
            }
        });
    }

    private void notifyDataChanged(){
        for (ClienteChangeListener listener : clienteChangeListeners){
            listener.onClienteChanged(cliente);
        }
    }

    public void updateDataForm() {

        if (servico == null) {
            throw new IllegalStateException("servico estava null");
        }

        List<Cliente> list = servico.findAll();
        obbList = FXCollections.observableArrayList(list);

        filterdClienteList = filteredTableView(obbList);

        tbvListaCliente.setItems(filterdClienteList);
        tbvListaCliente.refresh();
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
}
