package gui;

import db.DB;
import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Categoria;
import model.services.CategoriaServico;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriaCadastroController implements Initializable {

    private CategoriaServico servico;
    private Categoria categoria;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private Label lblId;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnLimpar;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtAbreviacao;

    public void onBtnCadastrarAction(ActionEvent event) {

        try {
            categoria = getFormData();
            servico.saveOrUpdate(categoria);
            Alerts.showAlert("Categoria Salva com sucesso !", null, "Categoria " + categoria.getNome()+ " salva com sucesso", Alert.AlertType.CONFIRMATION);
            notifyDataChanged();
            Utils.atualStage(event).close();
        } catch (DBException e){
            Alerts.showAlert("Erro ao salvar categoria", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    public void onBtnCancelarAction(ActionEvent event) {
        Utils.atualStage(event).close();
    }

    public void onBtnLimparAction() {
        limpaForm();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setLabeldInteger(lblId);
    }

    public void setCategoriaServico(CategoriaServico servico) {
        this.servico = servico;
    }

    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    public void updateTableView() {

        if (categoria == null){
            throw  new IllegalStateException("Categoria nao instanciada");
        }

        lblId.setText(String.valueOf(categoria.getId()));
        txtNome.setText(categoria.getNome());
        txtAbreviacao.setText(categoria.getAbreviacao());
    }

    private Categoria getFormData(){

        Categoria categoria = new Categoria();
        categoria.setId(Utils.converterInteiro(lblId.getText()));
        categoria.setNome(txtNome.getText());
        categoria.setAbreviacao(txtAbreviacao.getText());

        return categoria;

    }

    private void limpaForm(){
        txtNome.clear();
        txtAbreviacao.clear();
    }
}
