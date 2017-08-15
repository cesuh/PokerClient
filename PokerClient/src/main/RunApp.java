package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RunApp extends Application {

	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/runApp.fxml"));
		Pane root = (Pane) loader.load();
		Scene scene = new Scene(new Group(root));
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}
}
