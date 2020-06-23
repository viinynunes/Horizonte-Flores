package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import gui.util.ExportExcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Cliente;
import model.entities.Fornecedor;
import model.entities.Relatorio;
import model.services.ClienteServico;
import model.services.FornecedorServico;
import model.services.RelatorioServico;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioGeralPorClienteController implements Initializable {
    private FornecedorServico fornecedorServico;
    private RelatorioServico relatorioServico;
    private ClienteServico clienteServico;
    private List<Relatorio> relatorioList;
    private ObservableList<Relatorio> obbRelatorio;
    private Fornecedor fornecedor;
    private Cliente cliente;
    private Date date;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    @FXML
    private ComboBox<Cliente> cbbCliente;
    @FXML
    private DatePicker dpSelecionaData = new DatePicker();
    @FXML
    private Button btnGerarCliente;
    @FXML
    private Button btnGerarFornecedor;
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

    public void onBtnGerarClienteAction(){
        date = Date.valueOf(dpSelecionaData.getValue());

        if (clienteServico == null){
            throw new IllegalStateException("Cliente Servico null");
        }

        try {
            ObservableList<Cliente> obbList = FXCollections.observableArrayList(clienteServico.findByData(date));

            if (obbList.isEmpty()){
                Alerts.showAlert("Nenhum cliente encontrado", null, "Nenhum cliente encontrado", Alert.AlertType.INFORMATION);
                btnGerarFornecedor.setVisible(false);
                cbbFornecedor.setVisible(false);
                cbbCliente.setVisible(false);
                btnGerarRelatorio.setVisible(false);
                btnExportar.setVisible(false);

            } else {
                cbbCliente.setItems(obbList);
                cbbCliente.getSelectionModel().select(0);
                cbbCliente.setVisible(true);
                btnGerarFornecedor.setVisible(true);
            }


        }catch (DBException e){
            Alerts.showAlert("Erro ao buscar clientes", null, e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    public void onBtnGerarFornecedorAction() {
        date = Date.valueOf(dpSelecionaData.getValue());
        cliente = cbbCliente.getSelectionModel().getSelectedItem();

        if (fornecedorServico == null) {
            throw new IllegalStateException("fornecedor servico null");
        }

        try {
            ObservableList<Fornecedor> obbFornecedor = FXCollections.observableArrayList(fornecedorServico.findByClienteAndData(cliente, date));

            if (obbFornecedor.isEmpty()) {
                cbbFornecedor.setVisible(false);
                btnGerarRelatorio.setVisible(false);
                btnExportar.setVisible(false);
                Alerts.showAlert("Nenhum fornecedor encontrado", null, "Nenhum fornecedor encontrado", Alert.AlertType.INFORMATION);
            } else {
                cbbFornecedor.setItems(obbFornecedor);
                cbbFornecedor.getSelectionModel().select(0);
                if (obbRelatorio != null){
                    obbRelatorio.clear();
                }
                cbbFornecedor.setVisible(true);
                btnGerarRelatorio.setVisible(true);
                tbvListaRelatorio.setItems(obbRelatorio);
            }

        } catch (DBException e) {
            Alerts.showAlert("Erro ao buscar fornecedores", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtnGerarRelatorioAction() {
        fornecedor = getFormData();

        date = Date.valueOf(dpSelecionaData.getValue());

        relatorioList = relatorioServico.findByFornecedorAndCliente(fornecedor, cliente, date);
        obbRelatorio = FXCollections.observableArrayList(relatorioList);
        tbvListaRelatorio.setItems(obbRelatorio);
        tbvListaRelatorio.refresh();

        if (obbRelatorio.isEmpty()) {
            btnExportar.setVisible(false);
            Alerts.showAlert("Nenhum produto encontrado", null, "Nenhum produto encontrado", Alert.AlertType.INFORMATION);
        } else {
            btnExportar.setVisible(true);
            cbbFornecedor.setVisible(true);
            btnGerarRelatorio.setVisible(true);
        }
    }

    public void onBtnExportarAction() {
        if (relatorioList.isEmpty()) {
            Alerts.showAlert("Relatório Vazio", null, "O relatório esta vazio", Alert.AlertType.INFORMATION);
        } else {
            ExportExcel.createExcelRelatorio(relatorioList, fornecedor.getNome());
        }
    }

    public void onDpSelecionaDataAction(){
        cbbFornecedor.setVisible(false);
        cbbCliente.setVisible(false);
        btnGerarFornecedor.setVisible(false);
        btnGerarRelatorio.setVisible(false);
        btnExportar.setVisible(false);

        if (obbRelatorio != null){
            obbRelatorio.clear();
        }

        tbvListaRelatorio.refresh();
    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setRelatorioServico(RelatorioServico relatorioServico) {
        this.relatorioServico = relatorioServico;
    }

    public void setClienteServico(ClienteServico clienteServico) {
        this.clienteServico = clienteServico;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        dpSelecionaData.setValue(LocalDate.now());

        btnExportar.setVisible(false);
        cbbFornecedor.setVisible(false);
        btnGerarRelatorio.setVisible(false);
        cbbFornecedor.setVisible(false);
        btnGerarFornecedor.setVisible(false);
        cbbCliente.setVisible(false);

    }

    private Fornecedor getFormData() {
        return cbbFornecedor.getSelectionModel().getSelectedItem();
    }
}
