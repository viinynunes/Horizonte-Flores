package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.GridPane;

public class ClienteCadastroController {

	@FXML
	private Button btnCancelar;
	
	@FXML private GridPane gpForm;
	
	@FXML private ButtonBar btnBAcoes;

	@FXML
	public void onBtnCancelarAction() {
		System.out.println("BTN Cancelar");

	}

}
