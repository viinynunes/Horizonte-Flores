package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.entities.Sobra;
import model.services.FornecedorServico;
import model.services.ProdutoServico;
import model.services.SobraProdutoPadraoServico;
import model.services.SobraServico;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class SobraCaixariaController implements Initializable {

    private FornecedorServico fornecedorServico;
    private SobraServico sobraServico;
    private java.sql.Date iniDate, endDate;

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
    private TableColumn<Integer, Sobra> tbcTotalPedido;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalPedidoAtualizado;
    @FXML
    private TableColumn<Integer, Sobra> tbcSobra;
    @FXML
    private TableView<Sobra> tbvSobraFinal;
    @FXML
    private TableColumn<Sobra, Produto> tbcProdutoNomeFinal;
    @FXML
    private TableColumn<Integer, Sobra> tbcTotalFinal;
    @FXML
    private DatePicker datePicker1 = new DatePicker();
    @FXML
    private DatePicker datePicker2 = new DatePicker();
    @FXML
    private Button btnGerarFornecedores;
    @FXML
    private Button btnGerarRelatorio;
    @FXML
    private Button btnProdutosPadrao;

    @FXML
    public void onBtnGerarFornecedoresAction() {
        iniDate = java.sql.Date.valueOf(datePicker1.getValue());
        endDate = java.sql.Date.valueOf(datePicker2.getValue());

        try {
            ObservableList<Fornecedor> obbList = FXCollections.observableArrayList(fornecedorServico.findByData(iniDate, endDate));

            if (obbList.isEmpty()) {
                Alerts.showAlert("Nenhum fornecedor encontrado", null, "Nenhum fornecedor encontrado", Alert.AlertType.INFORMATION);
            } else {
                cbbFornecedor.setItems(obbList);
                cbbFornecedor.getSelectionModel().select(0);
            }
        } catch (DBException e) {
            Alerts.showAlert("Erro ao encontrar os fornecedores", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void onBtnGerarRelatorioAction() {
        Fornecedor fornecedor = cbbFornecedor.getSelectionModel().getSelectedItem();

        if (fornecedor == null) {
            Alerts.showAlert("Selecione um fornecedor", null, "Selecione um fornecedor", Alert.AlertType.INFORMATION);
        } else {
            try {

                List<Sobra> list = sobraServico.findByFornecedor(fornecedor, iniDate, endDate);
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
                Alerts.showAlert("Erro ao encontrar os produtos", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }


    @FXML
    public void onBtnProdutosPadraoAction(Event event){
        Stage parentStage = Utils.atualStage(event);
        carregaDialog(parentStage, "/gui/relatorio/SobraPadraoPorFornecedor.fxml", (SobraPadraoPorFornecedorController controller) ->{
            controller.setFornecedorServico(new FornecedorServico());
            controller.setProdutoServico(new ProdutoServico());
            controller.setSobraProdutoPadraoServico(new SobraProdutoPadraoServico());
            controller.updateCbbFornecedorFormData();
        });
    }

    @FXML
    public void onCbbFornecedorAction() {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tbcProdutoNome.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcTotalPedidoAtualizado.setCellValueFactory(new PropertyValueFactory<>("totalPedidoAtualizado"));
        tbcSobra.setCellValueFactory(new PropertyValueFactory<>("sobra"));

        //tableView Final
        tbcProdutoNomeFinal.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getProduto().getNome()));
        tbcTotalFinal.setCellValueFactory(new PropertyValueFactory<>("totalPedidoAtualizado"));

        datePicker1.setValue(LocalDate.now());
        datePicker2.setValue(LocalDate.now());

    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setSobraServico(SobraServico sobraServico) {
        this.sobraServico = sobraServico;
    }

    private void initTextFieldTotal() {

        tbcTotalPedido.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue()));

        tbcTotalPedido.setCellFactory(cell -> new TableCell<>() {

            TextField txtTotal = new TextField();

            @Override
            protected void updateItem(Sobra sobra, boolean empty) {
                super.updateItem(sobra, empty);
                if (sobra == null) {
                    setGraphic(null);
                    return;
                } else {
                    Constraints.setTextFieldInteger(txtTotal);
                    txtTotal.setText(sobra.getTotalPedido().toString());
                    setGraphic(txtTotal);

                    txtTotal.setOnAction(event -> {

                        System.out.println(txtTotal.getText());
                        Integer totalPedido = sobra.getTotalPedido();
                        Integer totalSobra = Utils.converterInteiro(txtTotal.getText()) - totalPedido;

                        sobra.setTotalPedidoAtualizado(Utils.converterInteiro(txtTotal.getText()));
                        sobra.setSobra(totalSobra);
                        sobraServico.insertOrUpdate(sobra);
                        tbvSobraCadastro.refresh();
                        tbvSobraFinal.refresh();
                        txtTotal.requestFocus();
                    });
                }
            }
        });
    }

    public synchronized <T> void carregaDialog(Stage parentStage, String caminho, Consumer<T> initConsumer) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
            VBox vBox = loader.load();

            Stage dialog = new Stage();

            T controller = loader.getController();
            initConsumer.accept(controller);

            dialog.setScene(new Scene(vBox));
            dialog.initOwner(parentStage);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
