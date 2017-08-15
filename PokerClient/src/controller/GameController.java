package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import clientNetwork.GameClient;
import javafx.scene.layout.Background;

public class GameController implements Initializable {

	@FXML
	private Pane Background;

	@FXML
	private Slider slideBetAmount;

	@FXML
	private TextField typeBetAmount;

	@FXML
	private Ellipse Table;

	@FXML
	private Label P1Name;

	@FXML
	private Label P2Name;

	@FXML
	private Label P3Name;

	@FXML
	private Label P4Name;

	@FXML
	private Label P5Name;

	@FXML
	private Label P6Name;

	@FXML
	private Label P1Stack;

	@FXML
	private Label P2Stack;

	@FXML
	private Label P3Stack;

	@FXML
	private Label P4Stack;

	@FXML
	private Label P5Stack;

	@FXML
	private Label P6Stack;

	@FXML
	private Rectangle P1Rectangle;

	@FXML
	private Rectangle P2Rectangle;

	@FXML
	private Rectangle P3Rectangle;

	@FXML
	private Rectangle P4Rectangle;

	@FXML
	private Rectangle P5Rectangle;

	@FXML
	private Rectangle P6Rectangle;

	@FXML
	private Button callButton;

	@FXML
	private Button raiseButton;

	@FXML
	private Button foldButton;

	@FXML
	private ImageView P1LeftCard;

	@FXML
	private ImageView P2LeftCard;

	@FXML
	private ImageView P3LeftCard;

	@FXML
	private ImageView P4LeftCard;

	@FXML
	private ImageView P5LeftCard;

	@FXML
	private ImageView P6LeftCard;

	@FXML
	private ImageView P1RightCard;

	@FXML
	private ImageView P2RightCard;

	@FXML
	private ImageView P3RightCard;

	@FXML
	private ImageView P4RightCard;

	@FXML
	private ImageView P5RightCard;

	@FXML
	private ImageView P6RightCard;

	@FXML
	private ImageView BoardCard1;

	@FXML
	private ImageView BoardCard2;

	@FXML
	private ImageView BoardCard3;

	@FXML
	private ImageView BoardCard4;

	@FXML
	private ImageView BoardCard5;

	@FXML
	private Label P1Bet;

	@FXML
	private Label P2Bet;

	@FXML
	private Label P3Bet;

	@FXML
	private Label P4Bet;

	@FXML
	private Label P5Bet;

	@FXML
	private Label P6Bet;

	@FXML
	private ImageView P1Dealer;

	@FXML
	private ImageView P2Dealer;

	@FXML
	private ImageView P3Dealer;

	@FXML
	private ImageView P4Dealer;

	@FXML
	private ImageView P5Dealer;

	@FXML
	private ImageView P6Dealer;

	@FXML
	private Label potSize;

	@FXML
	private Label BestHandText;

	@FXML
	private TextField chatTextField;

	@FXML
	private Button sendChatButton;

	@FXML
	private TextArea chatTextArea;

	@FXML
	private RadioButton TableGreen;

	@FXML
	private RadioButton TableRed;

	@FXML
	private RadioButton TableBlue;

	@FXML
	private RadioButton BackgroundThree;

	@FXML
	private RadioButton BackgroundTwo;

	@FXML
	private RadioButton BackgroundOne;

	@FXML
	private RadioButton ButtonRed;

	@FXML
	private RadioButton ButtonGray;

	@FXML
	private RadioButton ButtonBlack;

	@FXML
	private Button checkButton;

	@FXML
	private Button betButton;

	@FXML
	private Pane betInputPanel;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private ProgressBar progressBar1;

	@FXML
	private ProgressBar progressBar2;

	@FXML
	private ProgressBar progressBar3;

	@FXML
	private ProgressBar progressBar4;

	@FXML
	private ProgressBar progressBar5;

	@FXML
	private Label waitingForPlayersLabel;

	private BackgroundImage img1;
	private BackgroundImage img2;
	private BackgroundImage img3;

	private boolean setColorHelper = true;

	private ArrayList<Label> playerNames;

	private ArrayList<ProgressBar> progressBars;

	private ArrayList<Label> playerStacks;

	private ArrayList<Label> playerBets;

	private ArrayList<ImageView> leftCards;

	private ArrayList<ImageView> rightCards;

	private ArrayList<ImageView> dealerButtons;

	private ArrayList<ImageView> boardCards;

	private ArrayList<Rectangle> playerBoxes;

	private ToggleGroup tableColors;

	private ToggleGroup buttonColors;

	private ToggleGroup backgrounds;

	private GameClient client;

	private String toStringCard(int suit, int rank) {
		if (suit == 1)
			return "Spade" + rank;
		if (suit == 2)
			return "Heart" + rank;
		if (suit == 3)
			return "Diamond" + rank;
		return "Club" + rank;
	}

	private void setOriginalPlayerBoxColor(int tablePos) {
		playerBoxes.get(tablePos).setFill(Paint.valueOf("#ffffff80"));
		setColorHelper = true;
	}

	private void hideProgressBar(int tablePos) {
		progressBars.get(tablePos).setVisible(false);
	}

	private void setBetButtonText(String text) {
		betButton.setText("Bet " + text + "$");
	}

	private void setRaiseButtonText(String text) {
		raiseButton.setText("Raise:\n" + text + "$");
	}

	private void hideButtons() {
		foldButton.setVisible(false);
		betButton.setVisible(false);
		raiseButton.setVisible(false);
		checkButton.setVisible(false);
		callButton.setVisible(false);
		betInputPanel.setVisible(false);
		for (ProgressBar bar : progressBars)
			bar.setVisible(false);
	}

	public void setClient(GameClient client) {
		this.client = client;
	}

	public void writeToTextArea(String text) {
		if (!chatTextArea.getText().isEmpty())
			chatTextArea.setText(chatTextArea.getText() + " " + text);
		else
			chatTextArea.setText(chatTextArea.getText() + " " + text);

		chatTextArea.appendText("");
	}

	public void bet(int tablePos, int bet, int playerStack) {
		playerBets.get(tablePos).setText("$" + bet);
		setPlayerStack(tablePos, Integer.toString(playerStack));
		setOriginalPlayerBoxColor(tablePos);
	}

	public void setPlayerName(int tablePos, String name) {
		this.playerNames.get(tablePos).setText(name);
	}

	public void setPlayerStack(int tablePos, String value) {
		this.playerStacks.get(tablePos).setText("$" + value);
	}

	public void setPlayerCards(int tablePos, int leftRank, int leftSuit, int rightRank, int rightSuit) {
		Image leftImage = new Image(("pictures/cards/" + toStringCard(leftSuit, leftRank) + ".png"), 200, 100, true,
				true);
		Image rightImage = new Image(("pictures/cards/" + toStringCard(rightSuit, rightRank) + ".png"), 200, 100, true,
				true);
		leftCards.get(tablePos).setImage(leftImage);
		rightCards.get(tablePos).setImage(rightImage);
		dealCardsAudio.play();
	}

	public void setPlayerNoSound(int tablePos, int leftRank, int leftSuit, int rightRank, int rightSuit) {
		Image leftImage = new Image(("pictures/cards/" + toStringCard(leftSuit, leftRank) + ".png"), 200, 100, true,
				true);
		Image rightImage = new Image(("pictures/cards/" + toStringCard(rightSuit, rightRank) + ".png"), 200, 100, true,
				true);
		leftCards.get(tablePos).setImage(leftImage);
		rightCards.get(tablePos).setImage(rightImage);
	}

	public void removePlayerCards(int tablePos) {
		leftCards.get(tablePos).setImage(null);
		rightCards.get(tablePos).setImage(null);
		setOriginalPlayerBoxColor(tablePos);
	}

	public void removePlayerFromTable(int tablePos) {
		removePlayerCards(tablePos);
		setPlayerName(tablePos, "Empty seat");
		setPlayerStack(tablePos, "");
	}

	public void setAllOpponentPlayerCards(int tablePos, int numberOfPlayers) {
		Image img = new Image("pictures/cardbacks/Pomegranate.png");
		for (int i = 0; i < numberOfPlayers; i++)
			if (i != tablePos) {
				leftCards.get(i).setImage(img);
				rightCards.get(i).setImage(img);
			}
	}

	public void setPotSize(int pot) {
		potSize.setText("Pot size: $" + pot);
	}

	public void setBestHand(String text) {
		BestHandText.setText(text);
	}

	public void clearBoard() {
		BoardCard1.setImage(null);
		BoardCard2.setImage(null);
		BoardCard3.setImage(null);
		BoardCard4.setImage(null);
		BoardCard5.setImage(null);
	}

	public void setBoardCard(int boardPos, int rank, int suit) {
		Image image = new Image(("pictures/cards/" + toStringCard(suit, rank) + ".png"), 200, 100, true, true);
		boardCards.get(boardPos).setImage(image);
	}

	public void clearPlayerBets() {
		for (Label l : playerBets)
			l.setText("");
	}

	public void setDealer(int tablePos) {
		Image img = new Image("pictures/chip_dealer_top.png");
		for (ImageView iv : dealerButtons)
			iv.setImage(null);
		dealerButtons.get(tablePos).setImage(img);
	}

	public void reset() {
		clearBoard();
	}

	public void hideWaitingForPlayers() {
		waitingForPlayersLabel.setVisible(false);
	}

	public void displayButtons(int playerBet, int currentLargestBet, int bigBlindValue, int playerStack) {

		foldButton.setVisible(true);
		betInputPanel.setVisible(true);
		if (playerBet == currentLargestBet)
			checkButton.setVisible(true);
		else if (playerBet < currentLargestBet) {
			callButton.setVisible(true);
			callButton.setText("Call " + Integer.toString(currentLargestBet - playerBet) + "$");
		}
		if (currentLargestBet > 0) {
			raiseButton.setVisible(true);
			setRaiseButtonText(Integer.toString(currentLargestBet + bigBlindValue));
		} else {
			betButton.setVisible(true);
			setBetButtonText(Integer.toString(bigBlindValue));
		}
		if (currentLargestBet > 0)
			slideBetAmount.setMin(currentLargestBet + bigBlindValue);
		else
			slideBetAmount.setMin(bigBlindValue);
		slideBetAmount.setMax(playerStack + playerBet);
		slideBetAmount.setValue(slideBetAmount.getMin());
	}

	public void showProgressBar(int tablePos) {
		progressBars.get(tablePos).setVisible(true);
		progressBars.get(tablePos).setProgress(1);
	}

	public void changePlayerBoxColor(int tablePos) {
		if (setColorHelper) {
			playerBoxes.get(tablePos).setOpacity(0.7);
			setColorHelper = false;
		} else {
			playerBoxes.get(tablePos).setOpacity(1);
			setColorHelper = true;
		}
	}

	public void writeToChat(String text) {
		if (!chatTextArea.getText().trim().isEmpty())
			chatTextArea.setText(chatTextArea.getText() + "\n" + text);
		else
			chatTextArea.setText(text);
	}

	public void finishTurn(int tablePos) {
		setOriginalPlayerBoxColor(tablePos);
		hideProgressBar(tablePos);
	}

	public void clearBestHand() {
		BestHandText.setText("");
	}

	public void decrementProgress(int tablePos) {
		ProgressBar pb = progressBars.get(tablePos);
		pb.setProgress(pb.getProgress() - 0.004);
		if (progressBars.get(tablePos).getProgress() <= 0) {
			client.sendMessage("FOLD");
			hideButtons();
		}
	}

	private AudioClip dealCardsAudio;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		dealCardsAudio = new AudioClip(this.getClass().getResource("/soundEffects/cardPlace.wav").toExternalForm());

		playerNames = new ArrayList<Label>();
		playerNames.add(P1Name);
		playerNames.add(P2Name);
		playerNames.add(P3Name);
		playerNames.add(P4Name);
		playerNames.add(P5Name);
		playerNames.add(P6Name);
		for (Label name : playerNames)
			name.setText("Empty seat");
		playerStacks = new ArrayList<Label>();
		playerStacks.add(P1Stack);
		playerStacks.add(P2Stack);
		playerStacks.add(P3Stack);
		playerStacks.add(P4Stack);
		playerStacks.add(P5Stack);
		playerStacks.add(P6Stack);
		leftCards = new ArrayList<ImageView>();
		leftCards.add(P1LeftCard);
		leftCards.add(P2LeftCard);
		leftCards.add(P3LeftCard);
		leftCards.add(P4LeftCard);
		leftCards.add(P5LeftCard);
		leftCards.add(P6LeftCard);
		rightCards = new ArrayList<ImageView>();
		rightCards.add(P1RightCard);
		rightCards.add(P2RightCard);
		rightCards.add(P3RightCard);
		rightCards.add(P4RightCard);
		rightCards.add(P5RightCard);
		rightCards.add(P6RightCard);
		playerBets = new ArrayList<Label>();
		playerBets.add(P1Bet);
		playerBets.add(P2Bet);
		playerBets.add(P3Bet);
		playerBets.add(P4Bet);
		playerBets.add(P5Bet);
		playerBets.add(P6Bet);
		dealerButtons = new ArrayList<ImageView>();
		dealerButtons.add(P1Dealer);
		dealerButtons.add(P2Dealer);
		dealerButtons.add(P3Dealer);
		dealerButtons.add(P4Dealer);
		dealerButtons.add(P5Dealer);
		dealerButtons.add(P6Dealer);
		playerBoxes = new ArrayList<Rectangle>();
		playerBoxes.add(P1Rectangle);
		playerBoxes.add(P2Rectangle);
		playerBoxes.add(P3Rectangle);
		playerBoxes.add(P4Rectangle);
		playerBoxes.add(P5Rectangle);
		playerBoxes.add(P6Rectangle);
		progressBars = new ArrayList<ProgressBar>();
		progressBars.add(progressBar);
		progressBars.add(progressBar1);
		progressBars.add(progressBar2);
		progressBars.add(progressBar3);
		progressBars.add(progressBar4);
		progressBars.add(progressBar5);
		boardCards = new ArrayList<ImageView>();
		boardCards.add(BoardCard1);
		boardCards.add(BoardCard2);
		boardCards.add(BoardCard3);
		boardCards.add(BoardCard4);
		boardCards.add(BoardCard5);
		slideBetAmount.setMin(30);
		slideBetAmount.setMax(3000);
		slideBetAmount.setShowTickMarks(true);
		slideBetAmount.setMajorTickUnit(500);
		slideBetAmount.setMinorTickCount(0);
		tableColors = new ToggleGroup();
		TableGreen.setToggleGroup(tableColors);
		TableRed.setToggleGroup(tableColors);
		TableBlue.setToggleGroup(tableColors);
		buttonColors = new ToggleGroup();
		ButtonRed.setToggleGroup(buttonColors);
		ButtonGray.setToggleGroup(buttonColors);
		ButtonBlack.setToggleGroup(buttonColors);
		backgrounds = new ToggleGroup();
		BackgroundOne.setToggleGroup(backgrounds);
		BackgroundTwo.setToggleGroup(backgrounds);
		BackgroundThree.setToggleGroup(backgrounds);
		img1 = new BackgroundImage(new Image("pictures/backgrounds/Pane.jpg"), null, null, null, null);
		img2 = new BackgroundImage(new Image("pictures/backgrounds/Pane2.jpg"), null, null, null, null);
		img3 = new BackgroundImage(new Image("pictures/backgrounds/Pane3.jpg"), null, null, null, null);
		Background.setBackground(new Background(img1));
		hideButtons();

		slideBetAmount.valueProperty().addListener((observable, oldValue, newValue) -> {
			typeBetAmount.setText(String.valueOf((int) slideBetAmount.getValue()));
			if (raiseButton.isVisible())
				raiseButton.textProperty()
						.setValue("Raise: \n" + String.valueOf((int) slideBetAmount.getValue()) + "$");
			else
				betButton.textProperty().setValue("Bet: " + String.valueOf((int) slideBetAmount.getValue()) + "$");
		});

		TableRed.setOnAction(actionEvent -> {
			Table.setFill(Paint.valueOf("#a31010e8"));
		});
		TableGreen.setOnAction(actionEvent -> {
			Table.setFill(Paint.valueOf("#12a410e8"));
		});
		TableBlue.setOnAction(actionEvent -> {
			Table.setFill(Paint.valueOf("#060d6ee8"));
		});
		BackgroundOne.setOnAction(actionEvent -> {
			Background.setBackground(new Background(img1));
		});
		BackgroundTwo.setOnAction(actionEvent -> {
			Background.setBackground(new Background(img2));
		});
		BackgroundThree.setOnAction(actionEvent -> {
			Background.setBackground(new Background(img3));
		});

		sendChatButton.setOnAction(actionEvent -> {
			if (!chatTextField.getText().trim().isEmpty())
				client.sendMessage("CHATMESSAGE " + chatTextField.getText());
			chatTextField.clear();
		});

		foldButton.setOnAction(actionEvent -> {
			this.client.sendMessage("FOLD");
			hideButtons();
		});

		checkButton.setOnAction(actionEvent -> {
			this.client.sendMessage("CHECK");
			hideButtons();
		});

		betButton.setOnAction(actionEvent -> {
			this.client.sendMessage("BET " + typeBetAmount.getText());
			hideButtons();
		});

		raiseButton.setOnAction(actionEvent -> {
			this.client.sendMessage("BET " + typeBetAmount.getText());
			hideButtons();
		});

		callButton.setOnAction(actionEvent -> {
			this.client.sendMessage("CALL");
			hideButtons();
		});

		typeBetAmount.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				int bet = Integer.parseInt(newValue);
				if (raiseButton.isVisible())
					if (bet <= slideBetAmount.getMax())
						setRaiseButtonText(newValue);
					else {
						setRaiseButtonText(Integer.toString((int) slideBetAmount.getMax()));
						typeBetAmount.setText(Integer.toString((int) slideBetAmount.getMax()));
					}
				else
					setBetButtonText(newValue);
			} catch (NumberFormatException e) {
				typeBetAmount.setText(oldValue);
			}
		});
	}
}
