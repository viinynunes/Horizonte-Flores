package gui.relatorio;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.entities.Relatorio;
import model.services.FornecedorServico;
import model.services.RelatorioServico;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {
    private FornecedorServico fornecedorServico;
    private RelatorioServico relatorioServico;

    @FXML
    private ComboBox<Fornecedor> cbbFornecedor;
    @FXML
    private DatePicker datePicker1;
    @FXML
    private DatePicker datePicker2;
    @FXML
    private Button btnGerarRelatorio;
    @FXML
    private TableView<Relatorio> tbvListaRelatorio;
    @FXML
    private TableColumn<String, Relatorio> tbcProdutoNome;
    @FXML
    private TableColumn<Integer, Relatorio> tbcQuantidade;

    public void onBtnGerarRelatorioAction(){
        Fornecedor fornecedor = getFormData();

        List<Relatorio> list = relatorioServico.findByFornecedor(fornecedor);
        ObservableList<Relatorio> obbRelatorio = FXCollections.observableArrayList(list);
        tbvListaRelatorio.setItems(obbRelatorio);
        tbvListaRelatorio.refresh();

    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setRelatorioServico(RelatorioServico relatorioServico) {
        this.relatorioServico = relatorioServico;
    }

    public void updateFormData(){
        if (fornecedorServico == null){
            throw new IllegalStateException("fornecedor servico null");
        }

        if (relatorioServico == null){
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

    }

    private Fornecedor getFormData(){
        Fornecedor fornecedor = cbbFornecedor.getSelectionModel().getSelectedItem();

        return fornecedor;
    }
}
