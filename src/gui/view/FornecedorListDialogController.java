package gui.view;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.entities.Estabelecimento;
import model.entities.Fornecedor;
import model.services.EstabelecimentoServico;
import model.services.FornecedorServico;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class FornecedorListDialogController implements Initializable, DataChangeListener {

    private FornecedorServico fornecedorServico;
    private FilteredList<Fornecedor> filteredFornecedorList;

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnApagar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField txtLocaliza;
    @FXML
    private TableView<Fornecedor> tbvListaFornecedor;
    @FXML
    private TableColumn<Integer, Fornecedor> tbcFornecedorId;
    @FXML
    private TableColumn<String, Fornecedor> tbcFornecedorNome;
    @FXML
    private TableColumn<Fornecedor, Estabelecimento> tbcFornecedorEstabelecimento;

    public void onBtnNovoAction(Event event) {
        novoFornecedor(event);
    }

    private void novoFornecedor(Event event) {
        Fornecedor fornecedor = new Fornecedor();
        Stage parentStage = Utils.atualStage(event);

        carregaDialogTittledPane(parentStage, "/gui/view/FornecedorCadastro.fxml", (FornecedorCadastroController controller) -> {
            controller.setFornecedor(fornecedor);
            controller.setFornecedorServico(new FornecedorServico());
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.updateFormData();
            controller.subscribeDataChangeListener(this);
        });
    }

    public void onBtnEditarAction(Event event) {
        editarFornecedor(event);
    }

    private void editarFornecedor(Event event) {
        Stage parentStage = Utils.atualStage(event);
        Fornecedor fornecedor = tbvListaFornecedor.getSelectionModel().getSelectedItem();
        if (fornecedor == null) {
            Alerts.showAlert("Selecione um fornecedor", null, "Selecione um fornecedor", Alert.AlertType.INFORMATION);
        } else {
            carregaDialogTittledPane(parentStage, "/gui/view/FornecedorCadastro.fxml", (FornecedorCadastroController controller) -> {
                controller.setFornecedor(fornecedor);
                controller.setFornecedorServico(new FornecedorServico());
                controller.setEstabelecimentoServico(new EstabelecimentoServico());
                controller.updateFormData();
                controller.subscribeDataChangeListener(this);
            });
        }
    }

    public void onBtnApagarAction() {
        apagarFornecedor();
    }

    private void apagarFornecedor() {
        Fornecedor fornecedor = tbvListaFornecedor.getSelectionModel().getSelectedItem();
        if (fornecedor == null) {
            Alerts.showAlert("Selecione um fornecedor", null, "Selecione um fornecedor", Alert.AlertType.INFORMATION);
        } else {
            try {
                fornecedorServico.deleteById(fornecedor.getId());
                updateFormData();
            } catch (DBException e) {
                Alerts.showAlert("Erro ao deletar fornecedor", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void onBtnCancelarAction(Event event) {
        Stage parentStage = Utils.atualStage(event);
        parentStage.close();
    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbcFornecedorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcFornecedorNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcFornecedorEstabelecimento.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getEstabelecimento().getNome()));
    }

    public void updateFormData() {
        if (fornecedorServico == null) {
            throw new IllegalStateException("Fornecedor Servico null");
        }

        ObservableList<Fornecedor> obbList = FXCollections.observableArrayList(
                fornecedorServico.findAll()
        );

        filteredFornecedorList = filteredTableView(obbList);

        tbvListaFornecedor.setItems(filteredFornecedorList);
        tbvListaFornecedor.refresh();
        txtLocaliza.requestFocus();
    }

    public synchronized <T> void carregaDialogTittledPane(Stage parentStage, String caminho, Consumer<T> init) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            TitledPane pane = loader.load();
            Stage dialog = new Stage();

            T controller = loader.getController();
            init.accept(controller);

            dialog.setTitle("Cadastro");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FilteredList<Fornecedor> filteredTableView(ObservableList<Fornecedor> obbFornecedorList) {
        FilteredList<Fornecedor> filteredFornecedorList = new FilteredList<>(obbFornecedorList);

        txtLocaliza.textProperty().addListener(((observable, oldValue, newValue) -> {

            filteredFornecedorList.setPredicate(fornecedor -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (fornecedor.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (fornecedor.getEstabelecimento().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (fornecedor.getId().toString().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });

        }));

        return filteredFornecedorList;
    }

    @Override
    public void onDataChanged() {
        updateFormData();
    }
}
