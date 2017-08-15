package clientNetwork;

import java.io.IOException;

import controller.GameController;
import controller.LobbyController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LobbyClient extends Client implements Runnable {

	private LobbyController lc;
	private String name;
	private GameController gc;
	private FXMLLoader loader;
	private Pane root;
	private Scene scene;
	private Stage stage;
	
	public LobbyClient(int portNumber, String IP_ADDRESS, String name) throws IOException {
		super(portNumber, IP_ADDRESS);
		this.name = name;
		
		loader = new FXMLLoader(getClass().getResource("/fxml/Lobby.fxml"));
		root = (Pane) loader.load();
		scene = new Scene(new Group(root), 991, 655);
		stage = new Stage();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
		letterbox(scene, root);
		
		lc = (LobbyController) loader.getController();
		lc.setClient(this);
		String welcomeText = lc.getWelcomeMessage();
		String welcomeText2 = welcomeText.replace("NAME", name);
		String welcomeText3 = welcomeText2.replace("IP ADDRESS", IP_ADDRESS);
		lc.setWelcomeMessage(welcomeText3);
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
				this.in = null;
			}
			if (message != null) {
				String[] words = message.split(" ");
				if (words[0].equals("ADDGAMETOLIST")) {
					String gameInfo = "Name: " + words[1] + ", connected players: " + words[2] + "/" + words[3];
					Platform.runLater(() -> {
						lc.addGameToList(gameInfo);
					});
				} else if (words[0].equals("REQUESTJOINGAMEACCEPTED")) {
					Platform.runLater(() -> {
						try {
							lc.startGame(Integer.parseInt(words[1]), IP_ADDRESS);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
		}
	}
}