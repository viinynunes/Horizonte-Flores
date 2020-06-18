package gui.view;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.LoadPage;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Endereco;
import model.entities.Estabelecimento;
import model.services.EstabelecimentoServico;

import java.net.URL;
import java.util.ResourceBundle;

public class EstabelecimentoListDialogController implements Initializable, DataChangeListener {

    private EstabelecimentoServico estabelecimentoServico;
    private FilteredList<Estabelecimento> estabelecimentoFilteredList;

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
    private TableView<Estabelecimento> tbvListaEstabelecimento;
    @FXML
    private TableColumn<Integer, Estabelecimento> tbcEstabelecimentoId;
    @FXML
    private TableColumn<String, Estabelecimento> tbcEstabelecimentoNome;
    @FXML
    private TableColumn<Estabelecimento, Endereco> tbcEstabelecimentoEndereco;

    public void onBtnNovoAction(Event event){
        Stage parentStage = Utils.atualStage(event);
        Estabelecimento estabelecimento = new Estabelecimento();

        LoadPage.carregaDialogVBox(parentStage, "/gui/view/EstabelecimentoCadastro.fxml", (EstabelecimentoCadastroController controller)->{
            controller.setEstabelecimento(estabelecimento);
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.updateFormData();
            controller.subscribeChangeListener(this);
        });
    }

    public void onBtnEditarAction(Event event){
        Stage parentStage = Utils.atualStage(event);
        Estabelecimento estabelecimento = tbvListaEstabelecimento.getSelectionModel().getSelectedItem();

        if (estabelecimento == null){
            Alerts.showAlert("Selecione um estabelecimento", null, "Selecione um estabelecimento", Alert.AlertType.INFORMATION);
        } else {
            LoadPage.carregaDialogVBox(parentStage, "/gui/view/EstabelecimentoCadastro.fxml", (EstabelecimentoCadastroController controller)->{
                controller.setEstabelecimento(estabelecimento);
                controller.setEndereco(estabelecimento.getEndereco());
                controller.setEstabelecimentoServico(new EstabelecimentoServico());
                controller.updateFormData();
                controller.subscribeChangeListener(this);
            });
        }
    }

    public void onBtnApagarAction(){
        Estabelecimento estabelecimento = tbvListaEstabelecimento.getSelectionModel().getSelectedItem();

        if (estabelecimento == null){
            Alerts.showAlert("Selecione um estabelecimento", null, "Selecione um estabelecimento", Alert.AlertType.INFORMATION);
        } else {
            try {
                estabelecimentoServico.deleteById(estabelecimento.getId());
                updateFormData();
            }catch (DBException e){
                Alerts.showAlert("Erro ao Apagar Estabelecimento", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void onBtnCancelarAction(Event event){
        Utils.atualStage(event).close();
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcEstabelecimentoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcEstabelecimentoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcEstabelecimentoEndereco.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getEndereco().getLogadouro()));
    }

    public void updateFormData(){
        if (estabelecimentoServico == null){
            throw new IllegalStateException("Estabelecimento servico null");
        }

        ObservableList<Estabelecimento> obbList = FXCollections.observableArrayList(
          estabelecimentoServico.findAll()
        );

        estabelecimentoFilteredList = filteredTableView(obbList);
        tbvListaEstabelecimento.setItems(estabelecimentoFilteredList);
        tbvListaEstabelecimento.refresh();

    }

    private FilteredList<Estabelecimento> filteredTableView(ObservableList<Estabelecimento> obbEstabelecimentoList) {
        FilteredList<Estabelecimento> filteredCategoriaList = new FilteredList<>(obbEstabelecimentoList);

        txtLocaliza.textProperty().addListener(((observable, oldValue, newValue) -> {

            filteredCategoriaList.setPredicate(estabelecimento -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (estabelecimento.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (estabelecimento.getEndereco().getLogadouro().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (estabelecimento.getId().toString().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });

        }));

        return filteredCategoriaList;
    }

    @Override
    public void onDataChanged() {
        updateFormData();
    }
}
