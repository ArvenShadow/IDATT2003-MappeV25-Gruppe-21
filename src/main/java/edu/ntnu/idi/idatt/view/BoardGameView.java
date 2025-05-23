package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.action.TileAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import java.util.List;
import javafx.scene.Parent;

/**
 * Interface for defining the view component of a board game in an MVC architecture.
 * The BoardGameView is responsible for rendering the game state, displaying updates
 * related to players, moves, and game actions, and managing user interactions through handlers.
 */
public interface BoardGameView {
  void renderBoard(Board board);
  void updatePlayersList(List<Player> players);
  void movePlayer(Player player, int oldPosition, int newPosition);
  void movePlayerWithAnimation(Player player, int oldPosition, int newPosition, Runnable onComplete);
  void showDiceRoll(Player player, int roll);
  void showDiceRoll(Player player, int roll, int[] diceValues);
  void showAction(Player player, TileAction action);
  void showActionWithAnimation(Player player, TileAction action, int destinationTileId, Runnable onComplete);
  void highlightCurrentPlayer(Player player);
  void showGameOver(Player winner);

  void showError(String title, String message);
  void showMessage(String title, String message);

  String showLoadDialog();

  void updateDiceView(int diceCount);

  Parent getRoot();


  void setReturnToMenuHandler(Runnable handler);
  void setRollDiceHandler(Runnable handler);
  void setNewGameHandler(Runnable handler);
  void setLoadGameHandler(Runnable handler);

}