package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import clientNetwork.GameClient;
import clientNetwork.LobbyClient;

public class LobbyController implements Initializable {


	@FXML
	private Pane header;
	
	@FXML
	private Button joinGameButton, hostGameButton, closeApplicationButton, minimizeButton, fullScreenButton;

	@FXML
	private TextField passwordTextfield;

	@FXML
	private CheckBox passwordCheckbox;

	@FXML
	private RadioButton numberOfPlayers1, numberOfPlayers2, numberOfPlayers3, numberOfPlayers4, numberOfPlayers5;

	@FXML
	private RadioButton startingStack1, startingStack2, startingStack3, startingStack4, startingStack5;

	@FXML
	private RadioButton structure1, structure2, structure3, structure4, structure5;

	@FXML
	private Label OnlinePlayersLabel;

	@FXML
	private ListView<String> OnlinePlayersList, gameList;

	private ObservableList<String> gameData;

	private LobbyClient client;

	private ToggleGroup structures, numberOfPlayers, startingStacks;

	private String playerName;
	
	private Stage stage;

	public final void setStage(Stage stage) {
		this.stage = stage;
	}

	private int getSelectedNumberOfPlayers() {
		if (numberOfPlayers.getSelectedToggle().equals(numberOfPlayers1))
			return 2;
		else if (numberOfPlayers.getSelectedToggle().equals(numberOfPlayers2))
			return 3;
		else if (numberOfPlayers.getSelectedToggle().equals(numberOfPlayers3))
			return 4;
		else if (numberOfPlayers.getSelectedToggle().equals(numberOfPlayers4))
			return 5;
		else if (numberOfPlayers.getSelectedToggle().equals(numberOfPlayers5))
			return 6;
		return 0;
	}

	private void clearInput() {
		passwordTextfield.clear();
	}

	public void setClient(LobbyClient client) {
		this.client = client;
	}

	public void addGameToList(String game) {
		gameData.add(game);
		gameList.setItems(gameData);
	}

	public void startGame(int portNumber, String IP_address) throws IOException {
		GameClient gameClient = new GameClient(portNumber, IP_address);
		new Thread(gameClient).start();
		gameClient.sendMessage("SETNAME " + playerName);
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public void initialize(URL arg0, ResourceBundle arg1) {

		playerName = null;

		gameData = FXCollections.observableArrayList();

		structures = new ToggleGroup();
		structure1.setToggleGroup(structures);
		structure2.setToggleGroup(structures);
		structure3.setToggleGroup(structures);
		structure4.setToggleGroup(structures);
		structure5.setToggleGroup(structures);

		startingStacks = new ToggleGroup();
		startingStack1.setToggleGroup(startingStacks);
		startingStack2.setToggleGroup(startingStacks);
		startingStack3.setToggleGroup(startingStacks);
		startingStack4.setToggleGroup(startingStacks);
		startingStack5.setToggleGroup(startingStacks);

		numberOfPlayers = new ToggleGroup();
		numberOfPlayers1.setToggleGroup(numberOfPlayers);
		numberOfPlayers2.setToggleGroup(numberOfPlayers);
		numberOfPlayers3.setToggleGroup(numberOfPlayers);
		numberOfPlayers4.setToggleGroup(numberOfPlayers);
		numberOfPlayers5.setToggleGroup(numberOfPlayers);

		passwordTextfield.setEditable(false);
		passwordCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			passwordTextfield.setEditable(newValue);
			if (newValue)
				passwordTextfield.setStyle("-fx-background-color: white;");
			else {
				passwordTextfield.clear();
				passwordTextfield.setStyle("-fx-background-color: gray;");
			}
		});

		hostGameButton.setOnAction(actionEvent -> {
			int numberOfPlayers = getSelectedNumberOfPlayers();
			// String password = passwordTextfield.getText();
			client.sendMessage("REQUESTCREATEGAME " + numberOfPlayers);
			clearInput();
		});

		joinGameButton.setOnAction(actionEvent -> {
			if (gameList.getSelectionModel().getSelectedItem() != null)
				client.sendMessage("REQUESTJOINGAME " + gameList.getSelectionModel().getSelectedIndex());
		});
		final Delta dragDelta = new Delta();

		header.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = stage.getX() - mouseEvent.getScreenX();
				dragDelta.y = stage.getY() - mouseEvent.getScreenY();
			}
		});

		header.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				stage.setX(mouseEvent.getScreenX() + dragDelta.x);
				stage.setY(mouseEvent.getScreenY() + dragDelta.y);
			}
		});

		closeApplicationButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent actionEvent) {
				Platform.exit();
			}
		});

		minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				stage.setIconified(true);
			}
		});
	}
}