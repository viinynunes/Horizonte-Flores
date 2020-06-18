package gui.view;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.LoadPage;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Categoria;
import model.services.CategoriaServico;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriaListDialogController implements Initializable, DataChangeListener {

    private CategoriaServico categoriaServico;
    private FilteredList<Categoria> categoriaFilteredList;

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
    private TableView<Categoria> tbvListaCategoria;
    @FXML
    private TableColumn<Integer, Categoria> tbcCategoriaId;
    @FXML
    private TableColumn<String, Categoria> tbcCategoriaNome;
    @FXML
    private TableColumn<String, Categoria> tbcCategoriaAbreviacao;

    public void onBtnNovoAction(Event event) {
        Stage parentStage = Utils.atualStage(event);
        Categoria categoria = new Categoria();
        LoadPage.carregaDialogTittledPane(parentStage, "/gui/view/CategoriaCadastro.fxml", (CategoriaCadastroController controller) -> {
            controller.setCategoria(categoria);
            controller.setCategoriaServico(new CategoriaServico());
            controller.updateTableView();
            controller.subscribeDataChangeListener(this);
        });
    }

    public void onBtnEditarAction(Event event) {
        Stage parentStage = Utils.atualStage(event);
        Categoria categoria = tbvListaCategoria.getSelectionModel().getSelectedItem();

        if (categoria == null) {
            Alerts.showAlert("Selecione uma categoria", null, "Selecione uma categoria", Alert.AlertType.INFORMATION);
        } else {
            LoadPage.carregaDialogTittledPane(parentStage, "/gui/view/CategoriaCadastro.fxml", (CategoriaCadastroController controller) -> {
                controller.setCategoria(categoria);
                controller.setCategoriaServico(new CategoriaServico());
                controller.updateTableView();
                controller.subscribeDataChangeListener(this);
            });
        }
    }

    public void onBtnApagarAction() {
        Categoria categoria = tbvListaCategoria.getSelectionModel().getSelectedItem();
        if (categoria == null) {
            Alerts.showAlert("Selecione uma categoria", null, "Selecione uma categoria", Alert.AlertType.INFORMATION);
        } else {
            try {
                categoriaServico.deleteById(categoria.getId());
                updateFormData();
            } catch (DBException e) {
                Alerts.showAlert("Erro ao apagar categoria", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void onBtnCancelarAction(Event event) {
        Stage parentStage = Utils.atualStage(event);
        parentStage.close();
    }

    public void setCategoriaServico(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbcCategoriaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcCategoriaAbreviacao.setCellValueFactory(new PropertyValueFactory<>("abreviacao"));
    }

    public void updateFormData() {
        if (categoriaServico == null) {
            throw new IllegalStateException("categoria servico null");
        }

        ObservableList<Categoria> obbList = FXCollections.observableArrayList(
                categoriaServico.findAll()
        );

        categoriaFilteredList = filteredTableView(obbList);
        tbvListaCategoria.setItems(categoriaFilteredList);
        tbvListaCategoria.refresh();
    }

    private FilteredList<Categoria> filteredTableView(ObservableList<Categoria> obbCategoriaList) {
        FilteredList<Categoria> filteredCategoriaList = new FilteredList<>(obbCategoriaList);

        txtLocaliza.textProperty().addListener(((observable, oldValue, newValue) -> filteredCategoriaList.setPredicate(categoria -> {

            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            if (categoria.getNome().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (categoria.getAbreviacao().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else if (categoria.getId().toString().contains(lowerCaseFilter)) {
                return true;
            } else {
                return false;
            }
        })));

        return filteredCategoriaList;
    }


    @Override
    public void onDataChanged() {
        updateFormData();
    }
}
