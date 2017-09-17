package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import clientNetwork.LobbyClient;

public class LoginController implements Initializable {

	@FXML
	private Pane header;

	@FXML
	private Button closeApplicationButton;

	@FXML
	private Button minimizeButton;

	@FXML
	private Button fullScreenButton;
	
	@FXML
	private TextField usernameTextfield;

	@FXML
	private Button registerButton;

	@FXML
	private Button loginButton;

	@FXML
	private Label loginLogo;

	@FXML
	private PasswordField passwordField;
	
	private Stage stage;

	public final void setStage(Stage stage) {
		this.stage = stage;
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				usernameTextfield.requestFocus();
			}
		});

		loginButton.setOnAction(actionEvent -> {
			String username = usernameTextfield.getText();
			if (username.length() > 2)
				try {
					String ip_address = "10.0.0.168";
					LobbyClient lobbyClient = new LobbyClient(ip_address, username);
					new Thread(lobbyClient).start();
					loginButton.getScene().getWindow().hide();
				} catch (Exception e) {
					System.out.println("Something went wrong trying to join the server." + e);
				}
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
