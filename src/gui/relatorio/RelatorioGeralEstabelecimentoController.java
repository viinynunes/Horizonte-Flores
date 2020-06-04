package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import gui.util.ExportExcel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.*;
import model.services.EstabelecimentoServico;
import model.services.FornecedorServico;
import model.services.SobraServico;
import model.util.Utils;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioGeralEstabelecimentoController implements Initializable {

    private EstabelecimentoServico estabelecimentoServico;
    private SobraServico sobraServico;
    private java.sql.Date iniDate, endDate;
    private Estabelecimento estabelecimento;
    private List<Sobra> sobraList;

    @FXML
    private DatePicker dpInicial = new DatePicker();
    @FXML
    private DatePicker dpFinal = new DatePicker();
    @FXML
    private ComboBox<Estabelecimento> cbbEstabelecimento;
    @FXML
    private Button btnGerarRelatorio;
    @FXML
    private Button btnExportar;
    @FXML
    private TableView<Sobra> tbvRelatorio;
    @FXML
    private TableColumn<Sobra, Fornecedor> tbcNomeFornecedor;
    @FXML
    private TableColumn<Sobra, Produto> tbcNomeProduto;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalPedido;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalPedidoAtualizado;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalSobra;


    @FXML
    public void onBtnGerarRelatorioAction() {
        iniDate = Date.valueOf(dpInicial.getValue());
        endDate = Date.valueOf(dpFinal.getValue());
        estabelecimento = cbbEstabelecimento.getSelectionModel().getSelectedItem();

        try {

            sobraList = sobraServico.findByEstabelecimento(estabelecimento, iniDate, endDate);
            ObservableList<Sobra> obbList = FXCollections.observableArrayList(sobraList);
            tbvRelatorio.setItems(obbList);

        } catch (DBException e){
            Alerts.showAlert("Erro ao listar o relatório", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtnExportarAction(){
        Sobra sobra = sobraList.get(1);
        String nome = sobra.getProduto().getFornecedor().getEstabelecimento().getNome() + " " + sobra.getData().toString();
        ExportExcel.createExcelByEstabelecimento(sobraList, nome);
    }

    @FXML
    public void onCbbEstabelecimentoAction() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcNomeFornecedor.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getFornecedor().getNome()));
        tbcNomeProduto.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcTotalPedido.setCellValueFactory(new PropertyValueFactory<>("totalPedido"));
        tbcTotalPedidoAtualizado.setCellValueFactory(new PropertyValueFactory<>("totalPedidoAtualizado"));
        tbcTotalSobra.setCellValueFactory(new PropertyValueFactory<>("sobra"));

        dpInicial.setValue(LocalDate.now());
        dpFinal.setValue(LocalDate.now());

    }

    public void setSobraServico(SobraServico sobraServico) {
        this.sobraServico = sobraServico;
    }

    public void setEstabelecimentoServico(EstabelecimentoServico estabelecimentoServico) {
        this.estabelecimentoServico = estabelecimentoServico;
    }

    public void updateFormData(){
        if (estabelecimentoServico == null){
            throw new IllegalStateException("Estabelecimento Servico null");
        }

        ObservableList<Estabelecimento> obbList = FXCollections.observableArrayList(estabelecimentoServico.findAll());
        cbbEstabelecimento.setItems(obbList);
        cbbEstabelecimento.getSelectionModel().select(0);
    }
}
