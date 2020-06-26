package gui.relatorio;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Fornecedor;
import model.entities.Produto;
import model.entities.Sobra;
import model.entities.SobraProdutoPadrao;
import model.services.FornecedorServico;
import model.services.ProdutoServico;
import model.services.SobraProdutoPadraoServico;
import model.services.SobraServico;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class SobraCaixariaController implements Initializable {

    private ProdutoServico produtoServico;
    private FornecedorServico fornecedorServico;
    private SobraServico sobraServico;
    private SobraProdutoPadraoServico padraoServico;
    private java.sql.Date iniDate, endDate;
    private Fornecedor fornecedor;
    private FilteredList<Produto> filteredProdutoList;
    private List<Sobra> sobraList;

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
    private Button btnCarregaProdutosPadrao;
    @FXML
    private Button btnProdutosPadrao;
    @FXML
    private Button btnRemoverProduto;
    @FXML
    private TextField txtAdicionaProdutos;
    @FXML
    private ListView<Produto> lvAdicionaProdutos;
    @FXML
    private TextField txtQuantidadeSobra;

    @FXML
    public void onBtnGerarFornecedoresAction() {
        gerarfornecedores();
    }

    public void gerarfornecedores(){
        iniDate = java.sql.Date.valueOf(datePicker1.getValue());
        endDate = java.sql.Date.valueOf(datePicker2.getValue());

        tbvSobraCadastro.setItems(null);
        tbvSobraFinal.setItems(null);

        try {
            ObservableList<Fornecedor> obbList = FXCollections.observableArrayList(fornecedorServico.findByData(iniDate, endDate));

            if (obbList.isEmpty()) {
                Alerts.showAlert("Nenhum fornecedor encontrado", null, "Nenhum fornecedor encontrado", Alert.AlertType.INFORMATION);
            } else {
                cbbFornecedor.setItems(obbList);
                cbbFornecedor.getSelectionModel().select(0);
                fornecedor = cbbFornecedor.getItems().get(0);
                cbbFornecedor.setVisible(true);
                btnGerarRelatorio.setVisible(true);
                btnCarregaProdutosPadrao.setVisible(true);
            }
        } catch (DBException e) {
            Alerts.showAlert("Erro ao encontrar os fornecedores", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void onBtnGerarRelatorioAction() {
        gerarRelatorio();
    }

    private void gerarRelatorio(){
        fornecedor = cbbFornecedor.getSelectionModel().getSelectedItem();

        if (fornecedor == null) {
            Alerts.showAlert("Selecione um fornecedor", null, "Selecione um fornecedor", Alert.AlertType.INFORMATION);
        } else {
            atualizaTableView();
            btnRemoverProduto.setVisible(true);
        }
    }

    private void atualizaTableView(){
        try {
            sobraList = sobraServico.findByFornecedor(fornecedor, iniDate, endDate);
            ObservableList<Sobra> obbList = FXCollections.observableArrayList(sobraList);
            if (sobraList.isEmpty()) {
                tbvSobraCadastro.setItems(obbList);
                tbvSobraCadastro.refresh();
            } else {

                //for (Sobra sobra : obbList){
                  //  sobraServico.deleteBySobraAndDateLettingOneLine(sobra, iniDate, endDate);
                //}

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
    public void onBtnCarregaProdutosPadraoAction(){
        iniDate = java.sql.Date.valueOf(datePicker1.getValue());
        endDate = java.sql.Date.valueOf(datePicker2.getValue());
        try {
            ObservableList<SobraProdutoPadrao> obbProdutosPadrao = FXCollections.observableArrayList(
                    padraoServico.findAll());

            for (SobraProdutoPadrao padrao : obbProdutosPadrao){
                    Sobra sobra = new Sobra();
                    sobra.setData(new Date());
                    sobra.setProduto(padrao.getProduto());
                    sobra.setTotalPedido(0);
                    sobra.setTotalPedidoAtualizado(0);
                    sobra.setSobra(0);

                    sobraServico.insertOrUpdate(sobra, iniDate, endDate);
                    atualizaTableView();
                    gerarfornecedores();
            }

            gerarRelatorio();

        } catch (DBException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onBtnRemoverProdutoAction(){
        Sobra sobra = tbvSobraCadastro.getSelectionModel().getSelectedItem();

        if (sobra == null){
            Alerts.showAlert("Selecione um produto", null, "Selecione um produto", Alert.AlertType.INFORMATION);
        } else {
            try {
                sobraServico.deleteByDataAndProduto(iniDate, endDate, sobra.getProduto().getId());
                atualizaTableView();
                //gerarfornecedores();
            } catch (DBException e){
                Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
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

        txtAdicionaProdutos.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                adicionaProdutoSobra(event);
            }
        });

        txtQuantidadeSobra.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER){
                for (Sobra sobra : sobraList){
                    Integer totalPedido = sobra.getTotalPedido();
                    Integer totalSobra = Utils.converterInteiro(txtQuantidadeSobra.getText());

                    sobra.setTotalPedido(totalPedido);
                    sobra.setTotalPedidoAtualizado(Utils.converterInteiro(txtQuantidadeSobra.getText()) + sobra.getTotalPedido());
                    sobra.setSobra(totalSobra);

                    try {
                        sobraServico.insertWithDate(sobra, iniDate, endDate);
                        tbvSobraCadastro.refresh();
                        tbvSobraFinal.refresh();
                    } catch (DBException e){
                        Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            }
        });

        datePicker1.setValue(LocalDate.now());
        datePicker2.setValue(LocalDate.now());

        cbbFornecedor.setVisible(false);
        btnGerarRelatorio.setVisible(false);
        btnCarregaProdutosPadrao.setVisible(false);
        btnRemoverProduto.setVisible(false);
    }

        public void adicionaProdutoSobra(Event event){
            Produto p = lvAdicionaProdutos.getItems().get(0);

            if (p == null){
                Alerts.showAlert("Selecione um produto", null, "Selecione um produto", Alert.AlertType.INFORMATION);
            } else {
                try {
                    Sobra sobra = new Sobra();
                    sobra.setData(new Date());
                    sobra.setProduto(p);
                    sobra.setTotalPedido(0);
                    sobra.setTotalPedidoAtualizado(0);
                    sobra.setSobra(0);
                    sobraServico.insertOrUpdate(sobra, iniDate, endDate);
                    gerarfornecedores();
                    atualizaTableView();
                } catch (DBException e){
                    Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
                }
            }

        }

    public void updateFormData(){
        if (produtoServico == null){
            throw new IllegalStateException("Produto servico null");
        }

        ObservableList<Produto> obbProduto = FXCollections.observableArrayList(
                produtoServico.findAll()
        );

        filteredProdutoList = filteredTableView(obbProduto);
        lvAdicionaProdutos.setItems(filteredProdutoList);
        tbvSobraCadastro.refresh();
    }

    public void setFornecedorServico(FornecedorServico fornecedorServico) {
        this.fornecedorServico = fornecedorServico;
    }

    public void setSobraServico(SobraServico sobraServico) {
        this.sobraServico = sobraServico;
    }

    public void setPadraoServico(SobraProdutoPadraoServico padraoServico) {
        this.padraoServico = padraoServico;
    }

    public void setProdutoServico(ProdutoServico produtoServico) {
        this.produtoServico = produtoServico;
    }

    private void initTextFieldTotal() {

        tbcTotalPedido.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue()));

        tbcTotalPedido.setCellFactory(cell -> new TableCell<>() {

            TextField txtTotal = new TextField();
            List<Integer> iterator = new ArrayList<>();
            int aux = 0;

            @Override
            protected void updateItem(Sobra sobra, boolean empty) {
                super.updateItem(sobra, empty);
                if (sobra == null) {
                    setGraphic(null);
                    return;
                } else {

                    aux += aux;
                    iterator.add(aux);

                    Constraints.setTextFieldInteger(txtTotal);
                    txtTotal.setText(sobra.getTotalPedido().toString());
                    setGraphic(txtTotal);

                    txtTotal.setOnAction(event -> {

                        Integer totalPedido = sobra.getTotalPedido();
                        Integer totalSobra = Utils.converterInteiro(txtTotal.getText()) - totalPedido;

                        sobra.setTotalPedido(totalPedido);
                        sobra.setTotalPedidoAtualizado(Utils.converterInteiro(txtTotal.getText()));
                        sobra.setSobra(totalSobra);
                        sobraServico.insertWithDate(sobra, iniDate, endDate);
                        tbvSobraCadastro.refresh();
                        tbvSobraFinal.refresh();
                    });
                }
            }
        });
    }

    private FilteredList<Produto> filteredTableView(ObservableList<Produto> obbProdutoList){
        FilteredList<Produto> filteredProdutoList = new FilteredList<>(obbProdutoList);

        txtAdicionaProdutos.textProperty().addListener(((observable, oldValue, newValue) -> {

            this.filteredProdutoList.setPredicate(produto -> {

                if (newValue == null || newValue.isEmpty()){
                    lvAdicionaProdutos.setVisible(false);
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (produto.getNome().toLowerCase().contains(lowerCaseFilter)){
                    lvAdicionaProdutos.setVisible(true);
                    return true;
                } else if (produto.getFornecedor().getNome().toLowerCase().contains(lowerCaseFilter)){
                    lvAdicionaProdutos.setVisible(false);
                    return true;
                }else {
                    return false;
                }
            });

        }));

        return filteredProdutoList;
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
