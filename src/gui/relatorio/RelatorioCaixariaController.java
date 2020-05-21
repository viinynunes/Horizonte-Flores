package gui.relatorio;

import gui.util.Alerts;
import gui.util.ExportExcel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Estabelecimento;
import model.entities.Relatorio;
import model.services.EstabelecimentoServico;
import model.services.RelatorioServico;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioCaixariaController implements Initializable {
    private Estabelecimento estabelecimento;
    private EstabelecimentoServico estabelecimentoServico;
    private RelatorioServico relatorioServico;
    private List<Relatorio> relatorioList;

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
    @FXML
    private TableColumn<String, Relatorio> tbcFornecedor;
    @FXML
    private DatePicker datePicker1 = new DatePicker();
    @FXML
    private DatePicker datePicker2 = new DatePicker();

    public void onBtnGerarRelatorioAction(){
        if (relatorioServico == null){
            throw new IllegalStateException("Servico null");
        }

        Date iniDate = Date.valueOf(datePicker1.getValue());
        Date endDate = Date.valueOf(datePicker2.getValue());

        relatorioList = relatorioServico.findByEstabelecimento(estabelecimento, iniDate, endDate);
        ObservableList<Relatorio> obbList = FXCollections.observableArrayList(relatorioList);
        tbvListaRelatorio.setItems(obbList);
        if (obbList.isEmpty()){
            btnExportar.setVisible(false);
            Alerts.showAlert("Nenhum produto encontrado", null, "Nenhum produto encontrado", Alert.AlertType.INFORMATION);
        }else{
            btnExportar.setVisible(true);
        }
    }

    public void onBtnExportarAction(){
        if (relatorioList == null){
            Alerts.showAlert("Relatório Vazio", null, "O relatório esta vazio", Alert.AlertType.INFORMATION);
        } else {
            ExportExcel.createExcel(relatorioList, estabelecimento.getNome());
        }
    }

    public void setRelatorioServico(RelatorioServico relatorioServico) {
        this.relatorioServico = relatorioServico;
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
    }

    public void updateFormData(){
        estabelecimento = estabelecimentoServico.findById(4);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbcProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tbcFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

        datePicker1.setValue(LocalDate.now());
        datePicker2.setValue(LocalDate.now());

        btnExportar.setVisible(false);
    }
}
