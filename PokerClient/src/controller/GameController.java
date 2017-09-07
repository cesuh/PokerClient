package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	private CheckBox soundCheckBox;

	@FXML
	private Pane Background, betInputPanel;

	@FXML
	private Slider slideBetAmount;

	@FXML
	private TextField typeBetAmount;

	@FXML
	private Ellipse Table;

	@FXML
	private Label P1Name, P2Name, P3Name, P4Name, P5Name, P6Name;

	@FXML
	private Label P1Stack, P2Stack, P3Stack, P4Stack, P5Stack, P6Stack;

	@FXML
	private Rectangle P1Rectangle, P2Rectangle, P3Rectangle, P4Rectangle, P5Rectangle, P6Rectangle;

	@FXML
	private Button callButton, raiseButton, foldButton, sendChatButton, checkButton, betButton;

	@FXML
	private ImageView P1LeftCard, P2LeftCard, P3LeftCard, P4LeftCard, P5LeftCard, P6LeftCard;

	@FXML
	private ImageView P1RightCard, P2RightCard, P3RightCard, P4RightCard, P5RightCard, P6RightCard;

	@FXML
	private ImageView BoardCard1, BoardCard2, BoardCard3, BoardCard4, BoardCard5;

	@FXML
	private ImageView P1ProfilePic, P2ProfilePic, P3ProfilePic, P4ProfilePic, P5ProfilePic, P6ProfilePic;
	
	@FXML
	private ImageView P1Dealer, P2Dealer, P3Dealer, P4Dealer, P5Dealer, P6Dealer;

	@FXML
	private Label P1Bet, P2Bet, P3Bet, P4Bet, P5Bet, P6Bet;

	@FXML
	private Label potSize, bestHandText, waitingForPlayersLabel;

	@FXML
	private TextField chatTextField;

	@FXML
	private TextArea chatTextArea;

	@FXML
	private RadioButton TableBlack, TableBlue, TableGreen, TableRed;

	@FXML
	private RadioButton BackgroundOne, BackgroundTwo, BackgroundThree, BackgroundFour;
	
    @FXML
    private RadioButton Theme1, Theme2, Theme3;

	@FXML
	private ProgressBar progressBar, progressBar1, progressBar2, progressBar3, progressBar4, progressBar5;

	private BackgroundImage img1, img2, img3, img4;

	private boolean setColorHelper, sound = true;

	private ArrayList<Label> playerNames, playerStacks, playerBets;

	private ArrayList<ProgressBar> progressBars;

	private ArrayList<ImageView> leftCards, rightCards, dealerButtons, boardCards;

	private ArrayList<Rectangle> playerBoxes;

	private ToggleGroup tableColors, backgrounds, themes;

	private GameClient client;

	private AudioClip dealCardsAudio, foldCardsAudio, betChipsAudio, winChipsAudio, callChipsAudio, halfTimeAlarm,
			checkAudio;

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

	public void setPlayerCards(int tablePos, int leftSuit, int leftRank, int rightSuit, int rightRank) {
		Image leftImage = new Image(("pictures/cards/" + toStringCard(leftSuit, leftRank) + ".png"), 200, 100, true,
				true);
		Image rightImage = new Image(("pictures/cards/" + toStringCard(rightSuit, rightRank) + ".png"), 200, 100, true,
				true);
		leftCards.get(tablePos).setImage(leftImage);
		rightCards.get(tablePos).setImage(rightImage);

		if (sound) {
			dealCardsAudio.play();
		}
	}

	public void showDownCards(int tablePos, int leftSuit, int leftRank, int rightSuit, int rightRank) {
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
		bestHandText.setText(text);
	}

	public void clearBoard() {
		BoardCard1.setImage(null);
		BoardCard2.setImage(null);
		BoardCard3.setImage(null);
		BoardCard4.setImage(null);
		BoardCard5.setImage(null);
	}

	public void setBoardCard(int boardPos, int suit, int rank) {
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

	public void endTurn(int tablePos) {
		setOriginalPlayerBoxColor(tablePos);
		hideProgressBar(tablePos);
	}

	public void clearBestHand() {
		bestHandText.setText("");
	}

	public void decrementProgress(int tablePos) {
		ProgressBar pb = progressBars.get(tablePos);
		pb.setProgress(pb.getProgress() - 0.006);
		if (progressBars.get(tablePos).getProgress() <= 0) {
			client.sendMessage("FOLD");
			hideButtons();
		}
	}

	public void clearAllHands() {
		for (int i = 0; i < 6; i++)
			removePlayerCards(i);
	}

	public void playDealCardsAudio() {
		dealCardsAudio.play();
	}

	public void playFoldCardsAudio() {
		foldCardsAudio.play();
	}

	public void playBetAudio() {
		betChipsAudio.play();
	}

	public void playWinChipsAudio() {
		winChipsAudio.play();
	}

	public void playCallChipsAudio() {
		callChipsAudio.play();
	}

	public void playHalfTimeAlarm() {
		halfTimeAlarm.play();
	}

	public void playCheckAudio() {
		checkAudio.play();
	}

	public final boolean getSound() {
		return sound;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		dealCardsAudio = new AudioClip(this.getClass().getResource("/soundEffects/cardPlace.mp3").toExternalForm());
		foldCardsAudio = new AudioClip(this.getClass().getResource("/soundEffects/foldCards.wav").toExternalForm());
		betChipsAudio = new AudioClip(this.getClass().getResource("/soundEffects/BetChips.wav").toExternalForm());
		winChipsAudio = new AudioClip(this.getClass().getResource("/soundEffects/WinChips.wav").toExternalForm());
		callChipsAudio = new AudioClip(this.getClass().getResource("/soundEffects/CallChips.wav").toExternalForm());
		halfTimeAlarm = new AudioClip(this.getClass().getResource("/soundEffects/HalfTimeAlarm.wav").toExternalForm());
		checkAudio = new AudioClip(this.getClass().getResource("/soundEffects/Check.wav").toExternalForm());

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

		tableColors = new ToggleGroup();
		TableGreen.setToggleGroup(tableColors);
		TableRed.setToggleGroup(tableColors);
		TableBlue.setToggleGroup(tableColors);
		TableBlack.setToggleGroup(tableColors);
		tableColors.selectToggle(TableBlack);

		backgrounds = new ToggleGroup();
		BackgroundOne.setToggleGroup(backgrounds);
		BackgroundTwo.setToggleGroup(backgrounds);
		BackgroundThree.setToggleGroup(backgrounds);
		BackgroundFour.setToggleGroup(backgrounds);
		backgrounds.selectToggle(BackgroundOne);
		
		themes = new ToggleGroup();
		Theme1.setToggleGroup(themes);
		Theme2.setToggleGroup(themes);
		Theme3.setToggleGroup(themes);
		themes.selectToggle(Theme1);

		img1 = new BackgroundImage(new Image("pictures/backgrounds/black.jpg"), null, null, null, null);
		img2 = new BackgroundImage(new Image("pictures/backgrounds/Red-Carpet.jpg"), null, null, null, null);
		img3 = new BackgroundImage(new Image("pictures/backgrounds/black-on.jpg"), null, null, null, null);
		img4 = new BackgroundImage(new Image("pictures/backgrounds/Pane4.jpg"), null, null, null, null);
		Background.setBackground(new Background(img1));
		
		P1ProfilePic.setImage(new Image("pictures/avatar1.png"));
		P2ProfilePic.setImage(new Image("pictures/avatar2.png"));

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
		TableBlack.setOnAction(actionEvent -> {
			Table.setFill(Paint.valueOf("#090a0c"));
		});
		TableGreen.setOnAction(actionEvent -> {
			Table.setFill(Paint.valueOf("#12a410e8"));
		});
		TableBlue.setOnAction(actionEvent -> {
			Table.setFill(Paint.valueOf("#013fbb"));
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
		BackgroundFour.setOnAction(actionEvent -> {
			Background.setBackground(new Background(img4));
		});
		
		Theme1.setOnAction(actionEvent -> {
			client.changeToTheme1();
		});
		
		Theme2.setOnAction(actionEvent -> {
			client.changeToTheme2();
		});
		
		Theme3.setOnAction(actionEvent -> {
			client.changeToTheme3();
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

		soundCheckBox.setOnAction(actionEvent -> {
			if (sound)
				sound = false;
			else
				sound = true;
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
