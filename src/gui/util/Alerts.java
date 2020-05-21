package gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
	
	public static Alert showAlert(String title, String header, String content,AlertType type) {
		
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();

		return alert;
	}
}
