package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import db.DBException;
import gui.listeners.PedidoChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
import model.services.ClienteServico;
import model.services.ItemPedidoServico;
import model.services.PedidoServico;
import model.services.ProdutoServico;

public class MainViewController implements Initializable, PedidoChangeListener {

	private ItemPedidoServico itemServico = new ItemPedidoServico();
	private List<ItemPedido> itemPedidoList = new ArrayList<>();

	@FXML
	private MenuItem miFechar;
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
	public void onBtnNovoPedidoAction(ActionEvent event){
		System.out.println("Novo pedido");
		Stage parentStage = Utils.atualStage(event);
		Pedido pedido = new Pedido();
		List<ItemPedido> list = new ArrayList<>();
		carregaViewPedido(pedido, list, parentStage,"/gui/PedidoCadastro.fxml", (PedidoCadastroController controller) ->{});
	}

	@FXML
	public void onBtnEditarPedidoAction(ActionEvent event){
		try {
			Pedido pedido = tbvListaPedidos.getSelectionModel().getSelectedItem();
			itemPedidoList = itemServico.findAllPedidos(pedido);
			Stage parentStage = Utils.atualStage(event);
			carregaViewPedido(pedido, itemPedidoList, parentStage,"/gui/PedidoCadastro.fxml", (PedidoCadastroController controller) ->{});
		} catch (DBException e){
			Alerts.showAlert("Erro ao carregar a pagina", null, e.getMessage(), Alert.AlertType.ERROR);
		}

	}

	@FXML
	public void onBtnCancelarPedidoAction(){

	}

	@FXML
	public void onMiFecharAction() {
		System.exit(0);
	}
	
	@FXML
	public void onMiAbasProdutoAction() {
		System.out.println("Produto");
		carregaView("/gui/ProdutoList.fxml", (ProdutoListController controller) -> {
			controller.setProdutoServico(new ProdutoServico());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMiAbasClienteAction() {
		System.out.println("Cadastro Cliente");
		carregaView("/gui/ClienteList.fxml", (ClienteListController controller) -> {
			controller.setClienteServico(new ClienteServico());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuCadastroAction(){
		System.out.println("Cadastro");
		carregaView("/gui/Cadastro.fxml", (CadastroController controller)->{});
	}

	@FXML
	public void onMiRelPedidosAction() {
		System.out.println("Relat�rio Pedidos");
	}
	@FXML
	public void onMiRelCaixariaAction() {
		System.out.println("Relat�rio Caixaria");
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
		System.out.println("Relat�rio Geral");
	}

	@FXML
	public void onMiTransacoes(){
		System.out.println("Tela transações");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		tbcPedidoId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbcPedidoData.setCellValueFactory(new PropertyValueFactory<>("data"));
		tbcPedidoCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

		updateFormData();
	}

	public void updateFormData(){

		PedidoServico serv = new PedidoServico();

		List<Pedido> pedidoList = serv.findAll();
		ObservableList<Pedido> obb = FXCollections.observableArrayList(pedidoList);
		tbvListaPedidos.setItems(obb);
		tbvListaPedidos.refresh();
	}
	
	public synchronized <T> void carregaView(String caminho, Consumer<T> initializingAction) {

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
