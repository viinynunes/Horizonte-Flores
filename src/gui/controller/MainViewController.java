package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.controller.ClienteListController;
import gui.controller.ProdutoListController;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.services.ClienteServico;
import model.services.ProdutoServico;

public class MainViewController implements Initializable{
	
	
	@FXML
	private MenuItem miFechar;
	@FXML
	
	private MenuItem miCadProduto;
	@FXML
	private MenuItem miCadCliente;
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
	public void onMiFecharAction() {
		System.exit(0);
	}
	
	@FXML
	public void onMiCadProdutoAction() {
		System.out.println("Produto");
		carregaView("/gui/ProdutoList.fxml", (ProdutoListController controller) -> {
			controller.setProdutoServico(new ProdutoServico());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMiCadClienteAction() {
		System.out.println("Cadastro Cliente");
		carregaView("/gui/ClienteList.fxml", (ClienteListController controller) -> {
			controller.setClienteServico(new ClienteServico());
			controller.updateTableView();
		});
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
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
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
}
