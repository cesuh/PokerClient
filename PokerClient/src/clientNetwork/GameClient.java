package clientNetwork;

import java.io.IOException;

import controller.GameController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameClient extends Client {

	private GameController gc;
	private FXMLLoader loader;
	private Pane root;
	private Scene scene;
	private Stage stage;

	public GameClient(int portNumber, String IP_ADDRESS) throws IOException {
		super(portNumber, IP_ADDRESS);

		loader = new FXMLLoader(getClass().getResource("/fxml/Game.fxml"));
		root = (Pane) loader.load();
		scene = new Scene(new Group(root), 720, 500);

		stage = new Stage();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setMinWidth(736);
		stage.setMinHeight(538);
		stage.show();

		letterbox(scene, root);
		gc = (GameController) loader.getController();
		gc.setClient(this);

	}

	private boolean checkCard(int rank, int suit) {
		return rank >= 2 && rank <= 14 && suit >= 1 & suit <= 4;
	}

	private boolean checkPos(int pos) {
		return pos >= 0 && pos <= 5;
	}

	@Override
	public void run() {

		while (true) {
			String message = null;
			try {
				message = in.readUTF();
			} catch (IOException e) {
				this.in = null;
				System.out.println("Server closed from game client");
			}

			if (message != null) {
				String[] words = message.split(" ");
				final String msg = message;

				if (words[0].equals("FOLD")) {
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Fold tablePos out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Fold tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos)) {
							gc.removePlayerCards(tablePos);
							gc.finishTurn(tablePos);
						}
					});
				}

				else if (words[0].equals("CHANGEPLAYERBOXCOLOR")) {
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Change player box color tablePos out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println(
									"Change player box color tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos)) {
							gc.changePlayerBoxColor(tablePos);
						}
					});
				}

				else if (words[0].equals("CHECK")) {
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Check tablePos out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Check tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos)) {
							gc.finishTurn(tablePos);
						}
					});
				}

				else if (words[0].equals("UPDATEPOTSIZE")) {
					Platform.runLater(() -> {

						int potSize = -1;

						try {
							potSize = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Update potSize out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Update potSize unexpected type, expected int. Message = " + msg);
						}

						if (potSize >= 0) {
							gc.setPotSize(potSize);
						}
					});
				}

				else if (words[0].equals("BET"))
					Platform.runLater(() -> {

						int tablePos = -1;
						int bet = -1;
						int stack = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Bet tablePos index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Bet tablePos unexpected type, expected int. Message = " + msg);
						}

						try {
							bet = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Bet size index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Bet size unexpected type, expected int. Message = " + msg);
						}

						try {
							stack = Integer.parseInt(words[3]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("stack size index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Bet stack unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos) && bet >= 0 && stack >= 0) {
							gc.bet(tablePos, bet, stack);
							gc.finishTurn(tablePos);
						}
					});

				else if (words[0].equals("POTSIZE"))
					Platform.runLater(() -> {

						int potSize = -1;

						try {
							potSize = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Pot size index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Pot size unexpected type, expected int. Message = " + msg);
						}

						if (potSize >= 0)
							gc.setPotSize(potSize);
					});

				else if (words[0].equals("NAME")) {

					Platform.runLater(() -> {

						int tablePos = -1;
						String name = null;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Set name index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Set name tablePos unexpected type, expected int. Message = " + msg);
						}

						try {
							name = words[2];
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Set name index out of bounds. Message = " + msg);
						}

						if (checkPos(tablePos) && name != null)
							gc.setPlayerName(tablePos, name);
					});

				}

				else if (words[0].equals("DEALER"))
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Set deal button index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Set dealer tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos))
							gc.setDealer(tablePos);
					});

				else if (words[0].equals("PLAYERSTACK"))
					Platform.runLater(() -> {

						int tablePos = -1;
						int playerStack = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Set name index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println(
									"Set playerstack tablePos unexpected type, expected int. Message = " + msg);
						}

						try {
							playerStack = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Player stack index out of bounds. Message = " + msg);
						}

						if (checkPos(tablePos) && playerStack >= 0)
							gc.setPlayerStack(tablePos, words[2]);
					});

				else if (words[0].equals("DISPLAYBUTTONS"))
					Platform.runLater(() -> {

						int bet = -1;
						int currentBet = -1;
						int bigBlindValue = -1;
						int playerStack = -1;

						try {
							bet = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("display buttons bet size index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println(
									"Display buttons bet size unexpected type, expected int. Message = " + msg);
						}

						try {
							currentBet = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Current bet size index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Current bet size unexpected type, expected int. Message = " + msg);
						}

						try {
							bigBlindValue = Integer.parseInt(words[3]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Bigblind value index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Big blind value unexpected type, expected int. Message = " + msg);
						}

						try {
							playerStack = Integer.parseInt(words[4]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Player stack index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Player stack unexpected type, expected int. Message = " + msg);
						}

						if (bet >= 0 && currentBet >= 0 && bigBlindValue >= 0 && playerStack >= 0)
							gc.displayButtons(bet, currentBet, bigBlindValue, playerStack);
					});

				else if (words[0].equals("UNDECIDED"))
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Undecided tablePos index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Undecided tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos))
							gc.changePlayerBoxColor(tablePos);
					});

				else if (words[0].equals("PROGRESSBAR"))
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("progressBar tablePos index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("progressBar tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos))
							gc.decrementProgress(tablePos);
					});

				else if (words[0].equals("SHOWPROGRESSBAR"))
					Platform.runLater(() -> {

						int tablePos = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Show progressBar tablePos index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println(
									"Show progressBar tablePos unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos))
							gc.showProgressBar(tablePos);
					});
				else if (words[0].equals("DEALCARDS"))
					Platform.runLater(() -> {

						int tablePos = -1;
						int leftRank = -1;
						int leftSuit = -1;
						int rightRank = -1;
						int rightSuit = -1;
						int remainingPlayers = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Deal cards tablePos index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Deal cards tablePos unexpected type, expected int. Message = " + msg);
						}

						try {
							leftRank = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Left card rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Left card rank unexpected type, expected int. Message = " + msg);
						}

						try {
							leftSuit = Integer.parseInt(words[3]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Left card suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Left card suit unexpected type, expected int. Message = " + msg);
						}

						try {
							rightRank = Integer.parseInt(words[4]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Right card rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Left card rank unexpected type, expected int. Message = " + msg);
						}

						try {
							rightSuit = Integer.parseInt(words[5]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("right card suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("right card suit unexpected type, expected int. Message = " + msg);
						}

						try {
							remainingPlayers = Integer.parseInt(words[6]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Remaining players index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Remaining players unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos) && checkCard(leftRank, leftSuit) && checkCard(rightRank, rightSuit)
								&& remainingPlayers >= 2) {
							gc.setPlayerCards(tablePos, leftRank, leftSuit, rightRank, rightSuit);
							gc.setAllOpponentPlayerCards(tablePos, remainingPlayers);
						}
					});

				else if (words[0].equals("SHOWDOWN"))
					Platform.runLater(() -> {

						int tablePos = -1;
						int leftRank = -1;
						int leftSuit = -1;
						int rightRank = -1;
						int rightSuit = -1;
						int remainingPlayers = -1;

						try {
							tablePos = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Deal cards tablePos index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Deal cards tablePos unexpected type, expected int. Message = " + msg);
						}

						try {
							leftRank = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Left card rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Left card rank unexpected type, expected int. Message = " + msg);
						}

						try {
							leftSuit = Integer.parseInt(words[3]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Left card suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Left card suit unexpected type, expected int. Message = " + msg);
						}

						try {
							rightRank = Integer.parseInt(words[4]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Right card rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Left card rank unexpected type, expected int. Message = " + msg);
						}

						try {
							rightSuit = Integer.parseInt(words[5]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("right card suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("right card suit unexpected type, expected int. Message = " + msg);
						}

						try {
							remainingPlayers = Integer.parseInt(words[6]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Remaining players index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Remaining players unexpected type, expected int. Message = " + msg);
						}

						if (checkPos(tablePos) && checkCard(leftRank, leftSuit) && checkCard(rightRank, rightSuit)
								&& remainingPlayers >= 2) {
							gc.setPlayerNoSound(tablePos, leftRank, leftSuit, rightRank, rightSuit);
						}
					});

				else if (words[0].equals("DEALFLOP"))
					Platform.runLater(() -> {

						int card1Rank = -1;
						int card1Suit = -1;
						int card2Rank = -1;
						int card2Suit = -1;
						int card3Rank = -1;
						int card3Suit = -1;

						try {
							card1Rank = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card one rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card one rank unexpected type, expected int. Message = " + msg);
						}

						try {
							card1Suit = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card one suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card one suit unexpected type, expected int. Message = " + msg);
						}

						try {
							card2Rank = Integer.parseInt(words[3]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card two rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card two rank unexpected type, expected int. Message = " + msg);
						}

						try {
							card2Suit = Integer.parseInt(words[4]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card two suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card two suit unexpected type, expected int. Message = " + msg);
						}

						try {
							card3Rank = Integer.parseInt(words[5]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card three index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card three unexpected type, expected int. Message = " + msg);
						}

						try {
							card3Suit = Integer.parseInt(words[6]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card three suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card three suit unexpected type, expected int. Message = " + msg);
						}

						if (checkCard(card1Rank, card1Suit) && checkCard(card2Rank, card2Suit)
								&& checkCard(card3Rank, card3Suit)) {
							gc.setBoardCard(0, card1Rank, card1Suit);
							gc.setBoardCard(1, card2Rank, card2Suit);
							gc.setBoardCard(2, card3Rank, card3Suit);
						}
					});

				else if (words[0].equals("DEALTURN"))
					Platform.runLater(() -> {

						int card4Rank = -1;
						int card4Suit = -1;

						try {
							card4Rank = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card four rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card four rank unexpected type, expected int. Message = " + msg);
						}

						try {
							card4Suit = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card four suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card four suit unexpected type, expected int. Message = " + msg);
						}

						if (checkCard(card4Rank, card4Suit))
							gc.setBoardCard(3, card4Rank, card4Suit);
					});

				else if (words[0].equals("DEALRIVER"))
					Platform.runLater(() -> {
						int card5Rank = -1;
						int card5Suit = -1;

						try {
							card5Rank = Integer.parseInt(words[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card five rank index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card five rank unexpected type, expected int. Message = " + msg);
						}

						try {
							card5Suit = Integer.parseInt(words[2]);
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Card five suit index out of bounds. Message = " + msg);
						} catch (NumberFormatException e) {
							System.out.println("Card five suit unexpected type, expected int. Message = " + msg);
						}

						if (checkCard(card5Rank, card5Suit))
							gc.setBoardCard(4, card5Rank, card5Suit);
					});

				else if (words[0].equals("CLEARBETS"))
					Platform.runLater(() -> {
						gc.clearPlayerBets();
					});

				else if (words[0].equals("CHATMESSAGE")) {
					String chatMessage = message.replaceAll("CHATMESSAGE ", "");
					Platform.runLater(() -> {
						gc.writeToChat(chatMessage);
					});
				} else if (message.equals("HIDELABEL"))
					Platform.runLater(() -> {
						gc.hideWaitingForPlayers();
					});

				else if (message.equals("CLEARBOARD"))
					Platform.runLater(() -> {
						gc.clearBoard();
					});
				else if (words[0].equals("SETBESTHAND")) {
					String text = message.replaceAll("SETBESTHAND ", "");
					Platform.runLater(() -> {
						gc.setBestHand(text);
					});
				}
			}
		}
	}
}