package clientNetwork;

import controller.LoginController;
import controller.ResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginClient extends Application {

	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/runApp.fxml"));
		Pane root = (Pane) loader.load();
		Scene scene = new Scene(new Group(root));
		LoginController controller = loader.getController();
		controller.setStage(stage);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.sizeToScene();
		ResizeHelper listener = new ResizeHelper();
		listener.addResizeListener(stage);
		stage.show();
	}
}
