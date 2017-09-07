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
		scene = new Scene(new Group(root), 720, 510);
		stage = new Stage();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setMinWidth(720);
		stage.setMinHeight(510);
		stage.show();
		letterbox(scene, stage, root);
		gc = (GameController) loader.getController();
		gc.setClient(this);
		scene.getStylesheets().add(getClass().getResource("/fxml/theme1.css").toExternalForm());
	}

	public final void changeToTheme1() {
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/fxml/theme1.css").toExternalForm());
	}

	public final void changeToTheme2() {
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/fxml/theme2.css").toExternalForm());
	}

	public final void changeToTheme3() {
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/fxml/theme3.css").toExternalForm());
	}

	private boolean validateCard(int suit, int rank) {
		return rank >= 2 && rank <= 14 && suit >= 1 & suit <= 4;
	}

	private boolean checkTablePos(int pos) {
		return pos >= 0 && pos <= 5;
	}

	private int[] validateCardInfo(String suit, String rank) {
		int[] cardInfo = new int[2];

		try {
			cardInfo[0] = Integer.parseInt(suit);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Left suit rank index out of bounds. Message = " + suit);
		} catch (NumberFormatException e) {
			System.out.println("Left suit rank unexpected type, expected int. Message = " + suit);
		}

		try {
			cardInfo[1] = Integer.parseInt(rank);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Left card suit index out of bounds. Message = " + rank);
		} catch (NumberFormatException e) {
			System.out.println("Left card suit unexpected type, expected int. Message = " + rank);
		}

		if (validateCard(cardInfo[0], cardInfo[1]))
			return cardInfo;
		return null;
	}

	private final int validateTablePos(String tablePos) {
		int tablePosition = -1;
		try {
			tablePosition = Integer.parseInt(tablePos);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("TablePos out of bounds. Message = " + tablePos);
		} catch (NumberFormatException e) {
			System.out.println("TablePos unexpected type, expected int. Message = " + tablePos);
		}
		return tablePosition;
	}

	private final int validateChips(String msg) {
		int chips = -1;
		try {
			chips = Integer.parseInt(msg);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("TablePos out of bounds. Message = " + msg);
		} catch (NumberFormatException e) {
			System.out.println("TablePos unexpected type, expected int. Message = " + msg);
		}
		return chips;
	}

	@Override
	public void run() {

		while (true) {
			String message = null;
			try {
				message = in.readUTF();
			} catch (IOException e) {
				this.in = null;
			}

			if (message != null) {
				String[] words = message.split(" ");
				final String msg = message;

				if (words[0].equals("FOLD")) {
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos)) {
							gc.removePlayerCards(tablePos);
							gc.endTurn(tablePos);
							if (gc.getSound())
								gc.playFoldCardsAudio();
						}
					});
				}

				else if (words[0].equals("CHANGEPLAYERBOXCOLOR")) {
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos)) {
							gc.changePlayerBoxColor(tablePos);
						}
					});
				}

				else if (words[0].equals("CHECK")) {
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos)) {
							gc.endTurn(tablePos);
							if (gc.getSound())
								gc.playCheckAudio();
						}
					});
				}

				else if (words[0].equals("UPDATEPOTSIZE")) {
					Platform.runLater(() -> {
						int potSize = validateChips(words[1]);
						if (potSize > 0) {
							gc.setPotSize(potSize);
						}
					});
				}

				else if (words[0].equals("BET") || words[0].equals("CALL"))
					Platform.runLater(() -> {

						int tablePos = -1;
						int bet = -1;
						int stack = -1;

						tablePos = validateTablePos(words[1]);
						bet = validateChips(words[2]);
						stack = validateChips(words[3]);

						if (checkTablePos(tablePos) && bet > 0 && stack >= 0) {
							gc.bet(tablePos, bet, stack);
							gc.endTurn(tablePos);
							if (gc.getSound())
								if (words[0].equals("BET"))
									gc.playBetAudio();
								else
									gc.playCallChipsAudio();
						}
					});

				else if (words[0].equals("POTSIZE"))
					Platform.runLater(() -> {

						int potSize = validateChips(words[1]);

						if (potSize >= 0)
							gc.setPotSize(potSize);
					});

				else if (words[0].equals("NAME")) {

					Platform.runLater(() -> {

						int tablePos = validateTablePos(words[1]);
						String name = null;

						try {
							name = words[2];
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Set name index out of bounds. Message = " + msg);
						}
						if (checkTablePos(tablePos) && name != null)
							gc.setPlayerName(tablePos, name);
					});

				}

				else if (words[0].equals("DEALER"))
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos))
							gc.setDealer(tablePos);
					});

				else if (words[0].equals("PLAYERSTACK"))
					Platform.runLater(() -> {

						int tablePos = validateTablePos(words[1]);
						int playerStack = validateChips(words[2]);

						if (checkTablePos(tablePos) && playerStack >= 0)
							gc.setPlayerStack(tablePos, words[2]);
					});

				else if (words[0].equals("DISPLAYBUTTONS"))
					Platform.runLater(() -> {
						int bet = validateChips(words[1]);
						int currentBet = validateChips(words[2]);
						int bigBlindValue = validateChips(words[3]);
						int playerStack = validateChips(words[4]);
						if (bet >= 0 && currentBet >= 0 && bigBlindValue >= 0 && playerStack >= 0) {
							gc.displayButtons(bet, currentBet, bigBlindValue, playerStack);
						}
					});

				else if (words[0].equals("UNDECIDED"))
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos))
							gc.changePlayerBoxColor(tablePos);
					});

				else if (words[0].equals("PROGRESSBAR"))
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos))
							gc.decrementProgress(tablePos);
					});

				else if (words[0].equals("SHOWPROGRESSBAR"))
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						if (checkTablePos(tablePos))
							gc.showProgressBar(tablePos);
					});
				else if (words[0].equals("DEALCARDS"))
					Platform.runLater(() -> {

						int tablePos = validateTablePos(words[1]);
						int[] leftCard = validateCardInfo(words[2], words[3]);
						int[] rightCard = validateCardInfo(words[4], words[5]);
						int remainingPlayers = validateTablePos(words[6]);

						if (checkTablePos(tablePos) && leftCard != null && rightCard != null && remainingPlayers >= 2) {
							gc.setPlayerCards(tablePos, leftCard[0], leftCard[1], rightCard[0], rightCard[1]);
							gc.setAllOpponentPlayerCards(tablePos, remainingPlayers);
						}
					});

				else if (words[0].equals("SHOWDOWN"))
					Platform.runLater(() -> {
						int tablePos = validateTablePos(words[1]);
						int[] leftCard = validateCardInfo(words[2], words[3]);
						int[] rightCard = validateCardInfo(words[4], words[5]);
						int remainingPlayers = validateTablePos(words[6]);
						if (checkTablePos(tablePos) && leftCard != null && rightCard != null && remainingPlayers >= 2) {
							gc.showDownCards(tablePos, leftCard[0], leftCard[1], rightCard[0], rightCard[1]);
						}
					});

				else if (words[0].equals("DEALFLOP"))
					Platform.runLater(() -> {
						int[] card1 = validateCardInfo(words[1], words[2]);
						int[] card2 = validateCardInfo(words[3], words[4]);
						int[] card3 = validateCardInfo(words[5], words[6]);
						if (card1 != null && card2 != null && card3 != null) {
							gc.setBoardCard(0, card1[0], card1[1]);
							gc.setBoardCard(1, card2[0], card2[1]);
							gc.setBoardCard(2, card3[0], card3[1]);
						}
					});

				else if (words[0].equals("DEALTURN"))
					Platform.runLater(() -> {

						int[] turnCard = validateCardInfo(words[1], words[2]);

						if (turnCard != null)
							gc.setBoardCard(3, turnCard[0], turnCard[1]);
					});

				else if (words[0].equals("DEALRIVER"))
					Platform.runLater(() -> {

						int[] riverCard = validateCardInfo(words[1], words[2]);

						if (riverCard != null)
							gc.setBoardCard(4, riverCard[0], riverCard[1]);
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

				else if (message.equals("CLEARTABLE"))
					Platform.runLater(() -> {
						gc.clearBoard();
						gc.clearPlayerBets();
						gc.clearBestHand();
						gc.clearAllHands();
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