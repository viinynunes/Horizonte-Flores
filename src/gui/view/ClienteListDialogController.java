package gui.view;

import gui.listeners.ClienteChangeListener;
import gui.listeners.DataChangeListener;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Cliente;
import model.services.ClienteServico;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ClienteListDialogController implements Initializable, DataChangeListener {

    private ClienteServico servico;
    private Cliente cliente;
    private FilteredList<Cliente> filterdClienteList;
    private List<ClienteChangeListener> clienteChangeListeners = new ArrayList<>();

    @FXML
    private VBox vBox;
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


    public void onBtnNovoAction(Event event) {
        novoCliente(event);
    }

    private void novoCliente(Event event) {
        Stage parentStage = Utils.atualStage(event);
        Cliente cliente = new Cliente();
        carregaDialog(parentStage, "/gui/view/ClienteCadastro.fxml", (ClienteCadastroController controller) ->{
            controller.setCliente(cliente);
            controller.setServico(new ClienteServico());
            controller.subscribeDataChangeListener(this);
        });
    }

    public void setServico(ClienteServico servico) {
        this.servico = servico;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void subscribeDataChangeListener(ClienteChangeListener listener) {
        clienteChangeListeners.add(listener);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcClienteId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcClienteTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

        txtProcura.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                setCliente(tbvListaCliente.getItems().get(0));
                notifyDataChanged();
                Utils.atualStage(event).close();
            }

            if (event.getCode() == KeyCode.F2) {
                //carrega dialog cadastro de cliente
            }
        });

        tbvListaCliente.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                setCliente(tbvListaCliente.getSelectionModel().getSelectedItem());
                notifyDataChanged();
                Utils.atualStage(event).close();
            }
        });

        tbvListaCliente.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                setCliente(tbvListaCliente.getSelectionModel().getSelectedItem());
                notifyDataChanged();
                Utils.atualStage(event).close();
            }
        });

        vBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5){
                novoCliente(event);
            }
        });
    }

    private void notifyDataChanged() {
        for (ClienteChangeListener listener : clienteChangeListeners) {
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

    public synchronized <T> void carregaDialog(Stage parentStage, String caminho, Consumer<T> initConsumer) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            VBox vBox = loader.load();

            Stage dialog = new Stage();

            T controller = loader.getController();
            initConsumer.accept(controller);

            dialog.setScene(new Scene(vBox));
            dialog.initOwner(parentStage);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FilteredList<Cliente> filteredTableView(ObservableList<Cliente> obbClienteList) {
        FilteredList<Cliente> filteredClienteList = new FilteredList<>(obbClienteList);

        txtProcura.textProperty().addListener(((observable, oldValue, newValue) -> {

            filteredClienteList.setPredicate(cliente -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    tbvListaCliente.requestFocus();
                    txtProcura.requestFocus();
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
        updateDataForm();
    }
}
