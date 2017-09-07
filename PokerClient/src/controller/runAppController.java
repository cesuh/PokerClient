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
	private Button loginButton;

	@FXML
	private TextField passwordTextfield;

	@FXML
	private TextField usernameTextfield;

	@FXML
	private Label loginErrorLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				usernameTextfield.requestFocus();
				loginErrorLabel.setVisible(false);
			}
		});

		loginButton.setOnAction(actionEvent -> {
			String username = usernameTextfield.getText();
			try {
				String ip_address = "10.0.1.46";
				LobbyClient lobbyClient = new LobbyClient(1100, ip_address, username);
				new Thread(lobbyClient).start();
				loginButton.getScene().getWindow().hide();
			} catch (Exception e) {
				System.out.println("Something went wrong trying to join the server");
			}
		});
	}
}
