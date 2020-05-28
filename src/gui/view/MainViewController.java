package gui.view;

import application.Main;
import db.DBException;
import gui.listeners.KeyEventHandler;
import gui.listeners.PedidoChangeListener;
import gui.relatorio.RelatorioCaixariaController;
import gui.relatorio.RelatorioController;
import gui.relatorio.RelatorioPedidoController;
import gui.relatorio.SobraCaixariaController;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Cliente;
import model.entities.ItemPedido;
import model.entities.Pedido;
import model.entities.Produto;
import model.services.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable, PedidoChangeListener {

    private ItemPedidoServico itemServico = new ItemPedidoServico();
    private PedidoServico pedidoServico = new PedidoServico();
    private List<ItemPedido> itemPedidoList = new ArrayList<>();
    private static EventHandler<KeyEvent> keyEventEventHandler;
    KeyEventHandler eventHandler = new ClienteListController();
    LocalDate localDateDataPedido;
    java.sql.Date dataPedido;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private MenuItem miFechar;
    @FXML
    private MenuItem miPedido;
    @FXML
    private MenuItem miAbasProduto;
    @FXML
    private MenuItem miAbasCliente;
    @FXML
    private MenuItem miRelPedido;
    @FXML
    private MenuItem miRelCaixaria;
    @FXML
    private MenuItem miRelVeiling;
    @FXML
    private MenuItem miRelCeaflor;
    @FXML
    private MenuItem miRelGeral;
    @FXML
    private MenuItem miSobrasCaixaria;
    @FXML
    private MenuItem miTransacoes;
    @FXML
    private Button btnNovoPedido;
    @FXML
    private Button btnEditarPedido;
    @FXML
    private Button btnCancelarPedido;
    @FXML
    private ImageView imvLogo;
    @FXML
    private TableView<Pedido> tbvListaPedidos;
    @FXML
    private TableColumn<Integer, Pedido> tbcPedidoId;
    @FXML
    private TableColumn<Date, Pedido> tbcPedidoData;
    @FXML
    private TableColumn<Pedido, Cliente> tbcPedidoCliente;
    @FXML
    private DatePicker dpDataPedido = new DatePicker();

    public void onDpDataPedidoAction() {
        localDateDataPedido = dpDataPedido.getValue();
        dataPedido = java.sql.Date.valueOf(localDateDataPedido);
        updateFormData();
    }

    @FXML
    public void onBtnNovoPedidoAction(ActionEvent event) {
        novoPedido(event);
    }

    private void novoPedido(Event event) {
        System.out.println("Novo pedido");
        Stage parentStage = Utils.atualStage(event);
        Pedido pedido = new Pedido();
        List<ItemPedido> list = new ArrayList<>();
        carregaViewPedido(pedido, list, parentStage, "/gui/view/PedidoCadastro.fxml", (PedidoCadastroController controller) -> {
        });
    }

    @FXML
    public void onBtnEditarPedidoAction(ActionEvent event) {
        editarPedido(event);
    }

    private void editarPedido(Event event) {
        try {
            Pedido pedido = tbvListaPedidos.getSelectionModel().getSelectedItem();

            if (pedido == null) {
                Alerts.showAlert("Nenhum pedido selecionado", null, "Nenhum pedido selecionado", Alert.AlertType.INFORMATION);
            } else {
                itemPedidoList = itemServico.findAllPedidos(pedido);
                Stage parentStage = Utils.atualStage(event);
                carregaViewPedido(pedido, itemPedidoList, parentStage, "/gui/view/PedidoCadastro.fxml", (PedidoCadastroController controller) -> {
                });
            }

        } catch (DBException e) {
            Alerts.showAlert("Erro ao carregar a pagina", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onBtnCancelarPedidoAction() {
        cancelarPedido();
    }

    private void cancelarPedido() {

        Pedido pedido = tbvListaPedidos.getSelectionModel().getSelectedItem();

        if (pedido == null) {
            Alerts.showAlert("Nenhum pedido selecionado", null, "Nenhum pedido selecionado", Alert.AlertType.INFORMATION);
        } else {
            Alert alert = Alerts.showAlert("Confirmação", null, "Deseja realmente cancelar o pedido?", Alert.AlertType.CONFIRMATION);

            if (alert.getResult() == ButtonType.OK) {
                try {
                    pedido = tbvListaPedidos.getSelectionModel().getSelectedItem();
                    pedidoServico.deleteById(pedido);
                    updateFormData();
                } catch (DBException e) {
                    Alerts.showAlert("Erro ao cancelar pedido", null, e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    public void onMiFecharAction() {
        System.exit(0);
    }

    @FXML
    public void onMiPedidoAction() {
        Stage stage = Main.getStage();
        Main main = new Main();
        main.start(stage);
    }

    @FXML
    public void onMiAbasProdutoAction() {
        carregaView("/gui/view/ProdutoList.fxml", (ProdutoListController controller) -> {
            controller.setProdutoServico(new ProdutoServico());
            controller.updateTableView();
            eventHandler = controller;

        });
    }

    @FXML
    public void onMiAbasClienteAction() {
        carregaView("/gui/view/ClienteList.fxml", (ClienteListController controller) -> {
            controller.setClienteServico(new ClienteServico());
            controller.updateTableView();
            eventHandler = controller;
        });
    }

    @FXML
    public void onMenuCadastroAction() {
        System.out.println("Cadastro");
        carregaView("/gui/view/Cadastro.fxml", (CadastroController controller) -> {
            controller.setProduto(new Produto());
            controller.setCliente(new Cliente());
        });
    }

    @FXML
    public void onMiRelPedidosAction() {
        carregaView("/gui/relatorio/RelatorioPedido.fxml", (RelatorioPedidoController controller) -> {
            controller.setPedidoServico(new PedidoServico());
            controller.setItemServico(new ItemPedidoServico());
        });
    }

    @FXML
    public void onMiRelCaixariaAction() {
        carregaView("/gui/relatorio/RelatorioCaixaria.fxml", (RelatorioCaixariaController controller) -> {
            controller.setRelatorioServico(new RelatorioServico());
            controller.setEstabelecimentoServico(new EstabelecimentoServico());
            controller.updateFormData();
        });
    }

    @FXML
    public void onMiRelVeilingAction() {
        System.out.println("Relat�rio Veiling");
    }

    @FXML
    public void onMiRelCeaflorAction() {
        System.out.println("Relat�rio Ceaflor");
    }

    @FXML
    public void onMiRelGeralAction() {
        carregaView("/gui/relatorio/Relatorio.fxml", (RelatorioController controller) -> {
            controller.setFornecedorServico(new FornecedorServico());
            controller.setRelatorioServico(new RelatorioServico());
            controller.updateFormData();
        });
    }

    @FXML
    public void onMiSobrasCaixariaAction(){
        carregaView("/gui/relatorio/SobraCaixaria.fxml", (SobraCaixariaController controller) -> {
            controller.setFornecedorServico(new FornecedorServico());
            controller.setSobraServico(new SobraServico());
            controller.updateFormData();
        });
    }

    @FXML
    public void onMiTransacoes() {
        carregaView("/gui/view/TransacoesProduto.fxml", (TransacoesProdutoController controller) -> {
            controller.setProdutoServico(new ProdutoServico());
            controller.setItemPedidoServico(new ItemPedidoServico());
            controller.updateLocalizaProdutoFormData();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tbcPedidoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcPedidoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tbcPedidoCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        dpDataPedido.setValue(LocalDate.now());
        localDateDataPedido = dpDataPedido.getValue();
        dataPedido = java.sql.Date.valueOf(localDateDataPedido);

        scrollPane.addEventFilter(KeyEvent.KEY_PRESSED, getEventHandler());

        updateFormData();


    }

    private EventHandler<KeyEvent> getEventHandler() {
        keyEventEventHandler = event -> {

            if (event.getCode() == KeyCode.F2) {
                novoPedido(event);
            }
            if (event.getCode() == KeyCode.F3) {
                editarPedido(event);
            }
            if (event.getCode() == KeyCode.F4) {
                cancelarPedido();
            }
            if (event.getCode() == KeyCode.DELETE) {
                cancelarPedido();
            }
        };

        return keyEventEventHandler;
    }

    public void updateFormData() {

        PedidoServico serv = new PedidoServico();

        List<Pedido> pedidoList = serv.findByDate(dataPedido);
        ObservableList<Pedido> obb = FXCollections.observableArrayList(pedidoList);
        tbvListaPedidos.setItems(obb);
        tbvListaPedidos.refresh();
    }

    public synchronized <T> void carregaView(String caminho, Consumer<T> initializingAction) {

        scrollPane.removeEventFilter(KeyEvent.KEY_PRESSED, keyEventEventHandler);

        eventHandler.removeEventHandler();

        try {
            FXMLLoader load = new FXMLLoader(getClass().getResource(caminho));
            VBox novoVBox = load.load();

            Scene mainScene = Main.getScene();

            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);

            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(novoVBox.getChildren());

            T controller = load.getController();
            initializingAction.accept(controller);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized <T> void carregaViewPedido(Pedido pedido, List<ItemPedido> list, Stage parentStage, String caminho, Consumer<T> initConsumer) {

        try {
            FXMLLoader load = new FXMLLoader(getClass().getResource(caminho));
            VBox novoVBox = load.load();

            Stage dialog = new Stage();

            T cont = load.getController();
            initConsumer.accept(cont);

            PedidoCadastroController controller = load.getController();
            controller.setPedido(pedido);
            controller.setItemPedidoList(itemPedidoList);
            controller.setProdutoServico(new ProdutoServico());
            controller.setPedidoServico(new PedidoServico());
            controller.updateFormLocalizaProduto();
            controller.updateFormProdutosPedido();
            controller.subscribeDataChangeListener(this);

            dialog.setTitle("Pedido");
            dialog.setResizable(false);
            dialog.setScene(new Scene(novoVBox));
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setMaximized(true);
            dialog.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChangedListener(Pedido pedido) {
        updateFormData();
    }
}
