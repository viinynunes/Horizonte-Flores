package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private static Scene mainScene;
	private static Stage stage;

	@Override
	public void start(Stage stage) {
		try {

			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/gui/view/MainView.fxml"));
			ScrollPane scrollPanel = loader.load();

			scrollPanel.setFitToHeight(true);
			scrollPanel.setFitToWidth(true);

			mainScene = new Scene(scrollPanel);
			stage.setScene(mainScene);
			stage.setTitle("Horizonte Flores e Plantas");

			setStage(stage);

			Image icon = new Image(getClass().getResourceAsStream("/resources/imagens/HFP_logo.png"));
			stage.getIcons().add(icon);
			stage.setMaximized(true);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public static Scene getScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
