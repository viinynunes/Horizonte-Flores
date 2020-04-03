package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	private static Scene mainScene;

	@Override
	public void start(Stage stage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPanel = loader.load();

			scrollPanel.setFitToHeight(true);
			scrollPanel.setFitToWidth(true);

			mainScene = new Scene(scrollPanel);
			stage.setScene(mainScene);
			stage.setTitle("Horizonte Flores e Plantas");

			Image icon = new Image(getClass().getResourceAsStream("/resources/imagens/HFP_logo.jpg"));
			stage.getIcons().add(icon);
			stage.setMaximized(true);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Scene getScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
