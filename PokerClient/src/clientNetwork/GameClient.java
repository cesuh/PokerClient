package clientNetwork;

import java.io.IOException;

import controller.GameController;
import controller.ResizeHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
		scene = new Scene(new Group(root));
		stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.sizeToScene();
		stage.setMinWidth(720);
		stage.setMinHeight(535);
		ResizeHelper listener = new ResizeHelper();
		listener.addResizeListener(stage);
		stage.show();
		letterbox(scene, root);
		gc = (GameController) loader.getController();
		gc.setStage(stage);
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

	public final void changeToTheme4() {
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/fxml/theme4.css").toExternalForm());
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
			cardInfo[1] = Integer.parseInt(rank);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Something went wrong parsing the card info. Exception = " + e);
		}
		if (validateCard(cardInfo[0], cardInfo[1]))
			return cardInfo;
		return null;
	}

	private final int validateTablePos(String tablePos) {
		try {
			return Integer.parseInt(tablePos);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Something went wrong parsing the table position. Exception = " + e);
			return -1;
		}
	}

	private final int validateChips(String msg) {
		try {
			return Integer.parseInt(msg);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			System.out.println("Something went wrong parsing chip amount. Exception = " + e);
			return -1;
		}
	}

	@Override
	public void run() {

		while (true) {
			try {
				String message = in.readUTF();
				if (message != null) {

					String[] words = message.split(" ");
					String action = words[0];

					if (action.equals("UPDATEPOTSIZE")) {
						int potSize = validateChips(words[1]);
						Platform.runLater(() -> {
							if (potSize >= 0) {
								gc.setPotSize(potSize);
							}
						});
					}

					else if (action.equals("BET") || action.equals("CALL") || action.equals("RAISE")) {

						if (words.length != 4)
							System.out.println("Unexpected input. Input = " + message);
						else {
							int tablePos = validateTablePos(words[1]);
							int size = validateChips(words[2]);
							int stack = validateChips(words[3]);

							if (checkTablePos(tablePos) && size > 0 && stack >= 0)
								Platform.runLater(() -> {
									gc.bet(tablePos, size, stack);
									gc.endTurn(tablePos);
									if (gc.getSound())
										gc.playChipsAudio();
								});
						}
					}

					else if (action.equals("NAME")) {
						if (words.length != 3)
							System.out.println("Unexpected input for setting name. Input = " + message);
						else {
							int tablePos = validateTablePos(words[1]);
							String name = words[2];
							Platform.runLater(() -> {
								if (checkTablePos(tablePos) && name.length() >= 3)
									gc.setPlayerName(tablePos, name);
							});
						}
					}

					else if (action.equals("DEALER") || action.equals("UNDECIDED") || action.equals("PROGRESSBAR")
							|| action.equals("SHOWPROGRESSBAR") || action.equals("FOLD")
							|| action.equals("CHANGEPLAYERBOXCOLOR") || action.equals("CHECK")) {
						if (words.length != 2)
							System.out.println("Unexpected input. Input = " + message);
						else {
							int tablePos = validateTablePos(words[1]);
							if (checkTablePos(tablePos))
								Platform.runLater(() -> {
									if (action.equals("DEALER"))
										gc.setDealer(tablePos);

									else if (action.equals("UNDECIDED"))
										gc.changePlayerBoxColor(tablePos);

									else if (action.equals("PROGRESSBAR"))
										gc.decrementProgress(tablePos);

									else if (action.equals("FOLD")) {
										gc.removePlayerCards(tablePos);
										gc.endTurn(tablePos);
										if (gc.getSound())
											gc.playFoldCardsAudio();
									}

									else if (action.equals("CHANGEPLAYERBOXCOLOR"))
										gc.changePlayerBoxColor(tablePos);

									else if (action.equals("CHECK")) {
										gc.endTurn(tablePos);
										if (gc.getSound())
											gc.playCheckAudio();
									}

									else
										gc.showProgressBar(tablePos);
								});
						}
					}

					else if (action.equals("PLAYERSTACK")) {
						if (words.length != 3)
							System.out.println("Unexpected input for setting player stack. Input = " + message);
						else {
							int tablePos = validateTablePos(words[1]);
							int playerStack = validateChips(words[2]);
							if (checkTablePos(tablePos) && playerStack >= 0)
								Platform.runLater(() -> {
									gc.setPlayerStack(tablePos, words[2]);
								});
						}
					}

					else if (action.equals("DISPLAYBUTTONS")) {
						if (words.length != 5)
							System.out.println("Unexpected input for display buttons. Input = " + message);
						else {
							int bet = validateChips(words[1]);
							int currentBet = validateChips(words[2]);
							int bigBlindValue = validateChips(words[3]);
							int playerStack = validateChips(words[4]);
							if (bet >= 0 && currentBet >= 0 && bigBlindValue >= 0 && playerStack >= 0)
								Platform.runLater(() -> {
									gc.displayButtons(bet, currentBet, bigBlindValue, playerStack);
								});
						}
					}

					else if (action.equals("DEALCARDS")) {

						if (words.length != 7)
							System.out.println("Unexpected input dealcards. Input = " + message);
						else {
							int tablePos = validateTablePos(words[1]);
							int[] leftCard = validateCardInfo(words[2], words[3]);
							int[] rightCard = validateCardInfo(words[4], words[5]);
							int remainingPlayers = validateTablePos(words[6]);

							if (checkTablePos(tablePos) && leftCard != null && rightCard != null
									&& remainingPlayers >= 2)
								Platform.runLater(() -> {
									gc.setPlayerCards(tablePos, leftCard[0], leftCard[1], rightCard[0], rightCard[1]);
									gc.setAllOpponentPlayerCards(tablePos, remainingPlayers);
								});
						}
					}

					else if (action.equals("SHOWDOWN")) {
						if (words.length != 7)
							System.out.println("Unexpected input showdown. Input = " + message);
						else {
							int tablePos = validateTablePos(words[1]);
							int[] leftCard = validateCardInfo(words[2], words[3]);
							int[] rightCard = validateCardInfo(words[4], words[5]);
							int remainingPlayers = validateTablePos(words[6]);

							if (checkTablePos(tablePos) && leftCard != null && rightCard != null
									&& remainingPlayers >= 2) {
								Platform.runLater(() -> {
									gc.showDownCards(tablePos, leftCard[0], leftCard[1], rightCard[0], rightCard[1]);

								});
							}
						}
					}

					else if (action.equals("DEALFLOP")) {
						if (words.length != 7)
							System.out.println("Unexpected input deal flop. Input = " + message);
						else {
							int[] card1 = validateCardInfo(words[1], words[2]);
							int[] card2 = validateCardInfo(words[3], words[4]);
							int[] card3 = validateCardInfo(words[5], words[6]);
							if (card1 != null && card2 != null && card3 != null)
								Platform.runLater(() -> {
									gc.setBoardCard(0, card1[0], card1[1]);
									gc.setBoardCard(1, card2[0], card2[1]);
									gc.setBoardCard(2, card3[0], card3[1]);
								});
						}
					}

					else if (action.equals("DEALTURN") || action.equals("DEALRIVER")) {
						if (words.length != 3)
							System.out.println("Unexpected input deal turn/river. Input = " + message);
						else {
							int[] card = validateCardInfo(words[1], words[2]);
							if (card != null)
								Platform.runLater(() -> {
									if (action.equals("DEALTURN"))
										gc.setBoardCard(3, card[0], card[1]);
									else
										gc.setBoardCard(4, card[0], card[1]);
								});
						}
					}

					else if (action.equals("CLEARBETS"))
						Platform.runLater(() -> {
							gc.clearPlayerBets();
						});

					else if (action.equals("CHATMESSAGE")) {
						String chatMessage = message.replaceAll("CHATMESSAGE ", "");
						Platform.runLater(() -> {
							gc.writeToChat(chatMessage);
						});
					} else if (action.equals("HIDELABEL"))
						Platform.runLater(() -> {
							gc.hideWaitingForPlayers();
						});

					else if (action.equals("CLEARTABLE"))
						Platform.runLater(() -> {
							gc.clearBoard();
							gc.clearPlayerBets();
							gc.clearBestHand();
							gc.clearAllHands();
						});
					else if (action.equals("SETBESTHAND")) {
						String text = message.replaceAll("SETBESTHAND ", "");
						Platform.runLater(() -> {
							gc.setBestHand(text);
						});
					}
				}
			} catch (IOException e) {
				in = null;
				out = null;
				System.out.println("Something went wrong reading message from input socket. Exception = " + e);
			}
		}
	}
}