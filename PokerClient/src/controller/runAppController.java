package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import clientNetwork.LobbyClient;

public class runAppController implements Initializable {

	@FXML
	private Button hostServerButton;

	@FXML
	private Button joinServerButton;

	@FXML
	private TextField joinServerIPAddressTextfield;

	@FXML
	private TextField screenNameTextfield;

	@FXML
	private Label invalidNameLengthLabel;

	@FXML
	private Label joinServerErrorLabel;

	private boolean validLength(String name) {
		return name.length() >= 3 && name.length() <= 20;
	}

	private void hideErrors() {
		invalidNameLengthLabel.setVisible(false);
		joinServerErrorLabel.setVisible(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				screenNameTextfield.requestFocus();
			}
		});

		hideErrors();

		joinServerButton.setOnAction(actionEvent -> {
			String screenName = screenNameTextfield.getText();
			if (!joinServerIPAddressTextfield.getText().isEmpty() && validLength(screenName)) {
				try {
					String ip_address = joinServerIPAddressTextfield.getText();
					LobbyClient lobbyClient = new LobbyClient(1100, ip_address, screenName);
					new Thread(lobbyClient).start();
					joinServerButton.getScene().getWindow().hide();
				} catch (Exception e) {
					System.out.println("Something went wrong trying to join the server");
				}
			}
		});
	}
}
