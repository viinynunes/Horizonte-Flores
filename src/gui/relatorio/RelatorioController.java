package gui.relatorio;

import gui.util.Alerts;
import gui.util.ExportExcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Fornecedor;
import model.entities.Relatorio;
import model.services.FornecedorServico;
import model.services.RelatorioServico;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {
    private FornecedorServico fornecedorServico;
    private RelatorioServico relatorioServico;
    private List<Relatorio> relatorioList;
    private Fornecedor fornecedor;
    private Date iniDate, endDate;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    @FXML
    private DatePicker datePicker1 = new DatePicker();
    @FXML
    private DatePicker datePicker2 = new DatePicker();
    @FXML
    private Button btnGerarRelatorio;
    @FXML
    private Button btnExportar;
    @FXML
    private TableView<Relatorio> tbvListaRelatorio;
    @FXML
    private TableColumn<String, Relatorio> tbcProdutoNome;
    @FXML
    private TableColumn<Integer, Relatorio> tbcQuantidade;

    public void onBtnGerarRelatorioAction() {
        fornecedor = getFormData();

        iniDate = Date.valueOf(datePicker1.getValue());
        endDate = Date.valueOf(datePicker2.getValue());

        relatorioList = relatorioServico.findByFornecedor(fornecedor, iniDate, endDate);
        ObservableList<Relatorio> obbRelatorio = FXCollections.observableArrayList(relatorioList);
        tbvListaRelatorio.setItems(obbRelatorio);
        tbvListaRelatorio.refresh();
        if (obbRelatorio.isEmpty()) {
            Alerts.showAlert("Nenhum produto encontrado", null, "Nenhum produto encontrado", Alert.AlertType.INFORMATION);
        }
    }

    public void onBtnExportarAction() {
        if (tbvListaRelatorio == null) {
            Alerts.showAlert("Relatório Vazio", null, "O relatório esta vazio", Alert.AlertType.INFORMATION);
        } else if (relatorioList == null) {
            Alerts.showAlert("Relatório Vazio", null, "O relatório esta vazio", Alert.AlertType.INFORMATION);
        } else {
            ExportExcel.createExcel(relatorioList, fornecedor.getNome());
        }
    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setRelatorioServico(RelatorioServico relatorioServico) {
        this.relatorioServico = relatorioServico;
    }

    public void updateFormData() {
        if (fornecedorServico == null) {
            throw new IllegalStateException("fornecedor servico null");
        }

        if (relatorioServico == null) {
            throw new IllegalStateException("relatorio servico null");
        }

        ObservableList<Fornecedor> obbFornecedor = FXCollections.observableArrayList(fornecedorServico.findAll());
        cbbFornecedor.setItems(obbFornecedor);
        cbbFornecedor.getSelectionModel().select(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        datePicker1.setValue(LocalDate.now());
        datePicker2.setValue(LocalDate.now());

    }

    private Fornecedor getFormData() {
        return cbbFornecedor.getSelectionModel().getSelectedItem();
    }
}
