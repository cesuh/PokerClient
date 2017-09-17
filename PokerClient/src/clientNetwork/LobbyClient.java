package clientNetwork;

import java.io.IOException;
import controller.LobbyController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LobbyClient extends Client implements Runnable {

	private LobbyController lc;
	private String name;
	private FXMLLoader loader;
	private Pane root;
	private Scene scene;
	private Stage stage;
	
	public LobbyClient(String IP_ADDRESS, String name) throws IOException {
		super(1100, IP_ADDRESS);
		this.name = name;
		loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
		root = (Pane) loader.load();
		scene = new Scene(new Group(root));
		stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
		letterbox(scene, root);
		lc = (LobbyController) loader.getController();
		lc.setClient(this);
		lc.setStage(stage);
		lc.setPlayerName(name);
	}

	public String getName() {
		return name;
	}

	public LobbyController getLC() {
		return lc;
	}

	@Override
	public void run() {
		while (true) {
			String message = null;
			try {
				message = in.readUTF();
			} catch (Exception e) {
			}
			if (message != null) {
				String[] words = message.split(" ");
				if (words[0].equals("ADDGAMETOLIST")) {
					String gameInfo = "connected players: " + words[1] + "/" + words[2];
					Platform.runLater(() -> {
						lc.addGameToList(gameInfo);
					});
				} else if (words[0].equals("REQUESTJOINGAMEACCEPTED")) {
					Platform.runLater(() -> {
						try {
							lc.startGame(Integer.parseInt(words[1]), IP_ADDRESS);
						} catch (NumberFormatException e) {
							System.out.println("FAILED");
						} catch (IOException e) {
							System.out.println("FAILED2");
						}
					});
				}
			}
		}
	}
}