package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.entities.Sobra;
import model.services.FornecedorServico;
import model.services.SobraServico;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SobraCaixariaController implements Initializable {

    private FornecedorServico fornecedorServico;
    private SobraServico sobraServico;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    @FXML
    private TableView<Sobra> tbvSobraCadastro;
    @FXML
    private TableColumn<Integer, Sobra> tbcId;
    @FXML
    private TableColumn<Date, Sobra> tbcData;
    @FXML
    private TableColumn<Sobra, Produto> tbcProdutoNome;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotal;
    @FXML
    private TableColumn<Integer, Sobra> tbcSobra;
    @FXML
    private TableView<Sobra> tbvSobraFinal;
    @FXML
    private TableColumn<Sobra, Produto> tbcProdutoNomeFinal;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalFinal;

    @FXML
    public void onCbbFornecedorAction() {
        Fornecedor fornecedor = cbbFornecedor.getSelectionModel().getSelectedItem();

        try {
            List<Sobra> list = sobraServico.findByFornecedor(fornecedor);
            ObservableList<Sobra> obbList = FXCollections.observableArrayList(list);
            if (list.isEmpty()) {
                Alerts.showAlert("Nenhum produto encontrado", null, "Nenhum produto encontrado", Alert.AlertType.INFORMATION);
                tbvSobraCadastro.setItems(obbList);
                tbvSobraCadastro.refresh();
            } else {
                tbvSobraCadastro.setItems(obbList);
                tbvSobraCadastro.refresh();
                initTextFieldTotal();
            }

            tbvSobraFinal.setItems(obbList);
            tbvSobraFinal.refresh();

        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tbcProdutoNome.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        //tbcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tbcSobra.setCellValueFactory(new PropertyValueFactory<>("sobra"));

        //tableView Final
        tbcProdutoNomeFinal.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcTotalFinal.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setSobraServico(SobraServico sobraServico) {
        this.sobraServico = sobraServico;
    }

    public void updateFormData() {
        if (fornecedorServico == null) {
            throw new IllegalStateException("fornecedor servico null");
        }

        ObservableList<Fornecedor> obbList = FXCollections.observableArrayList(fornecedorServico.findAll());
        cbbFornecedor.setItems(obbList);
    }

    private void initTextFieldTotal(){
        tbcTotal.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue()));
        tbcTotal.setCellFactory(cell -> new TableCell<>(){
            private final TextField txtTotal = new TextField();

            @Override
            protected void updateItem(Sobra obj, boolean empty){
                super.updateItem(obj, empty);
                if (obj == null){
                    setGraphic(null);
                    return;
                } else {
                    txtTotal.setText(obj.getTotal().toString());
                    setGraphic(txtTotal);
                }
            }
        });
    }
}
