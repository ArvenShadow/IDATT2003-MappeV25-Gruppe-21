package edu.ntnu.idi.idatt.controller;


import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import edu.ntnu.idi.idatt.navigation.NavTo;
import edu.ntnu.idi.idatt.view.BoardGameView;
import edu.ntnu.idi.idatt.view.BoardGameViewImpl;

/**
 * The BoardGameController class acts as a controller component within
 * the MVC architecture of a board game application. It handles user
 * input, updates the model, and interacts with the view to ensure the
 * game progresses correctly.
 */

public class BoardGameController {
  private BoardGame model;
  private BoardGameView view;
  private boolean animationInProgress = false;

  /**
   * Constructs a new BoardGameController to manage interactions between the game model and view.
   *
   * @param model the BoardGame model containing the game's logic and state
   * @param view the BoardGameView interface responsible for rendering the game and handling user interactions
   */

  public BoardGameController(BoardGame model, BoardGameView view) {
    this.model = model;
    this.view = view;


    // Set up view event handlers
    view.setReturnToMenuHandler(this::handleReturnToMenu);
    view.setRollDiceHandler(this::handleRollDice);
    view.setNewGameHandler(this::handleNewGame);
    view.setLoadGameHandler(this::handleLoadGame);

    // Set up dice count change handler for integrated settings
    if (view instanceof BoardGameViewImpl) {
      ((BoardGameViewImpl) view).setDiceCountChangeHandler(this::handleDiceCountChange);
    }

    // Initialize view with current game state
    view.renderBoard(model.getBoard());
    view.updatePlayersList(model.getPlayers());

    // Highlight current player only if there are players
    if (!model.getPlayers().isEmpty()) {
      view.highlightCurrentPlayer(model.getCurrentPlayer());
    }
  }

  /**
   * Handles the logic for rolling the dice during a player's turn in the board game.
   *
   *<p>This method performs most of the steps necessary to complete a whole game turn.
   * It checks for actions, commands animations and so forth,
   * And updates the model and view accordingly
   */

  private void handleReturnToMenu() {
    try {
      model.getPlayers().clear();
      model.resetGameState();
      // Navigate back to character selection for full game setup
      edu.ntnu.idi.idatt.navigation.NavigationManager.getInstance()
          .navigateTo(NavTo.START_SCREEN);
    } catch (Exception e) {
      view.showError("Error starting new game", e.getMessage());
    }
  }

  private void handleRollDice() {
    if (animationInProgress) {
      return; // Prevent actions during animations
    }

    try {
      if (model.isFinished()) {
        view.showMessage("Game Over", "The game is already finished. Start a new game to play again.");
        return;
      }

      Player currentPlayer = model.getCurrentPlayer();

      // Check if player should skip turn
      if (currentPlayer.getSkipsNextTurn()) {
        currentPlayer.setSkipsNextTurn(false);
        view.showMessage("Skip Turn",
            currentPlayer.getName() + " skips this turn.");

        // Move to next player
        model.advanceToNextPlayer();
        view.highlightCurrentPlayer(model.getCurrentPlayer());
        return;
      }

      // First, roll the dice
      int[] diceValues = model.getDice().rollAllDice();
      int total = model.getDice().getTotal();

      // Show dice roll
      view.showDiceRoll(currentPlayer, total, diceValues);

      // Get current position
      int oldPosition = currentPlayer.getCurrentTile().getTileId();

      // Calculate new position
      int newPosition = oldPosition + total;
      int maxTileId = model.getBoard().getFinalTileId();

      // Handle case where player would move beyond the board
      if (newPosition > maxTileId) {
        newPosition = maxTileId;
      }

      // Get the destination tile
      final int destinationTileId = newPosition;

      // Start animation sequence
      animationInProgress = true;

      // 1. Animate player moving to the new position
      view.movePlayerWithAnimation(currentPlayer, oldPosition, destinationTileId, () -> {
        // After move animation completes, update model and check for tile action
        model.movePlayerToTile(currentPlayer, destinationTileId);

        // Get the tile the player landed on
        Tile landedTile = model.getBoard().getTile(destinationTileId);

        // Check if player landed on a tile with an action
        if (landedTile != null && landedTile.getTileAction() != null) {
          // Save the current position before action
          int preActionPosition = destinationTileId;

          // Execute the action in the model (this should update the player's position)
          landedTile.landAction(currentPlayer);

          // Get the new position after action
          int postActionPosition = currentPlayer.getCurrentTile().getTileId();

          // Only animate if positions different (action moved the player)
          if (preActionPosition != postActionPosition) {
            // 2. Animate the action (ladder/slide)
            view.showActionWithAnimation(currentPlayer, landedTile.getTileAction(), postActionPosition, () -> {
              finishTurn(currentPlayer);
            });
          } else {
            finishTurn(currentPlayer);
          }
        } else {
          finishTurn(currentPlayer);
        }
      });

    } catch (Exception e) {
      animationInProgress = false;
      view.showError("Error during turn", e.getMessage());
    }
  }


  /**
   * Completes the current player's turn in the board game.
   *
   *<p>The method checks if the current player has won the game by reaching the final tile.
   * If so, it updates the game state to finished and declares the winner.
   * Otherwise, the turn moves to the next player, and the view is updated to
   * highlight the new current player. Finally, it marks any animation sequence as complete.
   *
   * @param currentPlayer The player whose turn is being finished.
   */

  private void finishTurn(Player currentPlayer) {
    // Check if game is over
    if (currentPlayer.hasWon(model.getBoard().getFinalTileId())) {
      model.setWinner(currentPlayer);
      model.setGameFinished(true);
      view.showGameOver(currentPlayer);
    } else {
      // Move to next player
      model.advanceToNextPlayer();
      view.highlightCurrentPlayer(model.getCurrentPlayer());
    }

    // Animation sequence complete
    animationInProgress = false;
  }

  /**
   * Handles user interaction for starting a new game.
   *
   * <p>This method navigates the application to the character selection screen,
   * where a new game can be fully set up by selecting characters and other options.
   * If an error occurs during the navigation process, it displays an error message
   * to the user.
   */
  private void handleNewGame() {
    try {
      model.getPlayers().clear();
      model.resetGameState();
      // Navigate back to character selection for full game setup
      edu.ntnu.idi.idatt.navigation.NavigationManager.getInstance()
          .navigateTo(edu.ntnu.idi.idatt.navigation.NavTo.CHARACTER_SELECTION);
    } catch (Exception e) {
      view.showError("Error starting new game", e.getMessage());
    }
  }

  /**
   * Handles the loading of a saved game state into the board game.
   *
   * <p>This method facilitates the restoration of a previously saved game by
   * interacting with the view to prompt the user for a save file, loading the
   * game data into the model, and updating the view with the loaded game state.
   */
  private void handleLoadGame() {
    try {
      String filename = view.showLoadDialog();
      if (filename != null && !filename.isEmpty()) {
        model.loadGame(filename);
        view.renderBoard(model.getBoard());
        view.updatePlayersList(model.getPlayers());
        view.highlightCurrentPlayer(model.getCurrentPlayer());
        view.showMessage("Game Loaded", "Game successfully loaded from " + filename);
      }
    } catch (BoardGameException e) {
      view.showError("Error Loading Game", e.getMessage());
    }
  }

  /**
   * Handles changing the number of dice used in the game and updates the model and view accordingly.
   *
   * <p>This method checks if the requested dice count is different from the current dice count.
   * If so, it updates the model with the new number of dice and refreshes the view to reflect
   * the change. If an error occurs during this process, an error message is displayed using the view.
   *</p>
   *
   * @param newDiceCount the new number of dice to set in the game
   */
  private void handleDiceCountChange(int newDiceCount) {
    try {
      int currentDiceCount = model.getDice().getNumberOfDice();

      if (newDiceCount != currentDiceCount) {
        // Update the model
        model.getDice().setNumberOfDice(newDiceCount);

        // Update the view
        view.updateDiceView(newDiceCount);

        view.showMessage("Settings Updated",
            "Number of dice changed from " + currentDiceCount + " to " + newDiceCount);
      }
    } catch (Exception e) {
      view.showError("Error Updating Settings", e.getMessage());
    }
  }

}