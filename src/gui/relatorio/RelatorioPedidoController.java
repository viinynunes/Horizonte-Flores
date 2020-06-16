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
import model.entities.Cliente;
import model.entities.ItemPedido;
import model.entities.Pedido;
import model.services.ItemPedidoServico;
import model.services.PedidoServico;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RelatorioPedidoController implements Initializable {

    private PedidoServico pedidoServico;
    private ItemPedidoServico itemServico;
    private java.sql.Date iniDate, endDate;

    @FXML
    private Button btnGerarRelatorio;
    @FXML
    private Button btnExportar;
    @FXML
    private DatePicker datePicker1 = new DatePicker();
    @FXML
    private DatePicker datePicker2 = new DatePicker();
    @FXML
    private TableView<Pedido> tbvPedido;
    @FXML
    private TableColumn<Integer, Pedido> tbcId;
    @FXML
    private TableColumn<Date, Pedido> tbcData;
    @FXML
    private TableColumn<Pedido, Cliente> tbcClienteNome;
    private List<Pedido> pedidoList;
    private Set<ItemPedido> itemPedidoList;
    private Set<ItemPedido> itemPedidoListFinal = new LinkedHashSet<>();
    private List<Integer> countList = new ArrayList<>();

    @FXML
    public void onBtnGerarRelatorioAction(){
        iniDate = java.sql.Date.valueOf(datePicker1.getValue());
        endDate = java.sql.Date.valueOf(datePicker2.getValue());

        if (pedidoServico == null){
            throw new IllegalStateException("Pedido servico null");
        }

        try {
            pedidoList = pedidoServico.findByDate(iniDate, endDate);
            ObservableList<Pedido> obbList = FXCollections.observableArrayList(pedidoList);
            tbvPedido.setItems(obbList);
            tbvPedido.refresh();

            int count = 0;

            for (Pedido p : pedidoList){

                itemPedidoList = itemServico.findByData(p.getCliente(), iniDate, endDate);
                for (ItemPedido i : itemPedidoList){
                    itemPedidoListFinal.add(i);
                    count++;
                }
                countList.add(count);
                count = 0;
            }

            if (obbList.isEmpty()){
                Alerts.showAlert("Nenhum pedido encontrado", null, "Nenhum pedido encontrado", Alert.AlertType.INFORMATION);
                btnExportar.setVisible(false);
            } else {
                btnExportar.setVisible(true);
            }

        } catch (DBException e){
            Alerts.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
            btnExportar.setVisible(false);
        } finally {
            pedidoList.clear();
        }
    }

    @FXML
    private void onBtnExportarAction(){
        ExportExcel.createExcelPedido(itemPedidoListFinal, countList, iniDate.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tbcClienteNome.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCliente().getNome()));

        datePicker1.setValue(LocalDate.now());
        datePicker2.setValue(LocalDate.now());

        btnExportar.setVisible(false);

    }

    public void setPedidoServico(PedidoServico pedidoServico) {
        this.pedidoServico = pedidoServico;
    }

    public void setItemServico(ItemPedidoServico itemServico) {
        this.itemServico = itemServico;
    }
}
