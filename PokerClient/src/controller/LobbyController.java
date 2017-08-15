package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import clientNetwork.GameClient;
import clientNetwork.LobbyClient;

public class LobbyController implements Initializable {

	@FXML
	private Button joinGameButton;

	@FXML
	private Button hostGameButton;

	@FXML
	private TextField passwordTextfield;

	@FXML
	private TextField gameNameTextfield;

	@FXML
	private CheckBox passwordCheckbox;

	@FXML
	private RadioButton numberOfPlayers5;

	@FXML
	private RadioButton numberOfPlayers1;

	@FXML
	private RadioButton numberOfPlayers2;

	@FXML
	private RadioButton numberOfPlayers3;

	@FXML
	private RadioButton numberOfPlayers4;

	@FXML
	private RadioButton startingStack4;

	@FXML
	private RadioButton startingStack5;

	@FXML
	private RadioButton startingStack3;

	@FXML
	private RadioButton startingStack2;

	@FXML
	private RadioButton startingStack1;

	@FXML
	private RadioButton structure1;

	@FXML
	private RadioButton structure2;

	@FXML
	private RadioButton structure3;

	@FXML
	private RadioButton structure4;

	@FXML
	private RadioButton structure5;

	@FXML
	private Label welcomeLabel;

	@FXML
	private Label gameNameError;

	@FXML
	private Label joinGameError;

	@FXML
	private Label OnlinePlayersLabel;

	@FXML
	private ListView<String> OnlinePlayersList;

	@FXML
	private ListView<String> gameList;

	private ObservableList<String> gameData;

	private LobbyClient client;

	private ToggleGroup structures;

	private ToggleGroup startingStacks;

	private ToggleGroup numberOfPlayers;
	
	private String playerName;
	
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
		gameNameTextfield.clear();
		passwordTextfield.clear();
	}

	private void clearErrors() {
		gameNameError.setVisible(false);
		joinGameError.setVisible(false);
	}

	public String getWelcomeMessage() {
		return welcomeLabel.getText();
	}

	public void setWelcomeMessage(String message) {
		welcomeLabel.setText(message);
	}

	public void setClient(LobbyClient client) {
		this.client = client;
	}

	public void addGameToList(String game) {
		gameData.add(game);
		gameList.setItems(gameData);
	}
	
	public void startGame(int portNumber, String IP_address) throws IOException{
		GameClient gameClient = new GameClient(portNumber, IP_address);
		new Thread(gameClient).start();
		gameClient.sendMessage("SETNAME " + playerName);
	}
	
	public void setPlayerName(String name){
		this.playerName = name;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		playerName = null;

		gameData = FXCollections.observableArrayList();

		clearErrors();

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
			clearErrors();
			if (gameNameTextfield.getText().length() >= 3) {
				String gameName = gameNameTextfield.getText().replaceAll(" ", "");
				int numberOfPlayers = getSelectedNumberOfPlayers();
				// String password = passwordTextfield.getText();
				client.sendMessage("REQUESTCREATEGAME " + gameName + " " + numberOfPlayers);
				clearInput();
			}
		});

		joinGameButton.setOnAction(actionEvent -> {
			clearErrors();
			if (gameList.getSelectionModel().getSelectedItem() != null) {
				client.sendMessage("REQUESTJOINGAME " + 0);
			} else
				joinGameError.setVisible(true);
		});

	}
}