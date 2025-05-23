package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.action.LadderAction;
import edu.ntnu.idi.idatt.action.TileAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.view.ingame.BoardView;
import edu.ntnu.idi.idatt.view.ingame.DiceView;
import edu.ntnu.idi.idatt.view.ingame.PlayerInfoView;
import edu.ntnu.idi.idatt.view.ingame.SettingsPanel;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;


/**
 * Implementation of the {@code BoardGameView} interface to represent the UI components
 * and interactions for a board game. Facilitates rendering of the game board, managing
 * players, and providing visual feedback for game events such as dice rolls and tile
 * actions.
 */
public class BoardGameViewImpl implements BoardGameView {
  private BorderPane root;
  private StackPane centerStack; // Stack to overlay settings on board
  private StackPane mainStack; // Main stack to overlay Clippy on everything
  private BoardView boardView;
  private PlayerInfoView playerInfoView;
  private DiceView diceView;
  private Label statusLabel;
  private Button menuButton;
  private Button rollButton;
  private Button newGameButton;
  private Button loadButton;
  private Button settingsButton;
  private HBox controls;

  private SettingsPanel settingsPanel;
  private ClippyNotification clippyNotification;
  private boolean settingsVisible = false;

  private Runnable returnToMenu;
  private Runnable rollDiceHandler;
  private Runnable newGameHandler;
  private Runnable loadGameHandler;
  private Consumer<Integer> diceCountChangeHandler;

  private BoardGame model;

  /**
   * Constructs a new instance of the BoardGameViewImpl class, initializing the
   * view components and associating it with the provided board game model.
   *
   * @param model The {@code BoardGame} model that this view interacts with. It
   *              contains the game state, board configuration, and game logic.
   */
  public BoardGameViewImpl(BoardGame model) {
    this.model = model;
    createUI();
  }

  /**
   * Initializes and sets up the user interface for the board game view. This method
   * constructs various UI components shown to the user in the game view.
   */
  private void createUI() {
    // Create main stack pane to hold everything including Clippy
    mainStack = new StackPane();

    root = new BorderPane();
    root.setPadding(new Insets(20));

    // Status bar at top
    statusLabel = new Label("Game ready to start");
    statusLabel.setId("statusText");

    settingsButton = new Button("Settings");
    settingsButton.setOnAction(e -> toggleSettings());

    HBox topBar = new HBox(10, statusLabel, settingsButton);
    topBar.setAlignment(Pos.CENTER);
    root.setTop(topBar);

    // Center stack for board and settings overlay
    centerStack = new StackPane();

    // Board view
    boardView = new BoardView(model.getBoard());
    centerStack.getChildren().add(boardView);

    // Settings panel (initially hidden)
    settingsPanel = new SettingsPanel(model.getDice().getNumberOfDice());
    settingsPanel.setVisible(false);
    settingsPanel.setManaged(false);

    settingsPanel.setOnDiceCountChanged(count -> {
      if (diceCountChangeHandler != null) {
        diceCountChangeHandler.accept(count);
      }
    });

    settingsPanel.setOnClose(() -> hideSettings());

    centerStack.getChildren().add(settingsPanel);
    StackPane.setAlignment(settingsPanel, Pos.TOP_CENTER);

    root.setCenter(centerStack);

    // Player info on right
    playerInfoView = new PlayerInfoView(model.getPlayers(), boardView);
    root.setRight(playerInfoView);

    // Game controls at bottom
    menuButton = new Button("Main Menu");
    rollButton = new Button("Roll Dice");
    newGameButton = new Button("New Game");
    loadButton = new Button("Load Game");

    diceView = new DiceView(model.getDice().getNumberOfDice());

    menuButton.setOnAction(e -> {
      if (returnToMenu != null) {
        returnToMenu.run();
      }
    });

    rollButton.setOnAction(e -> {
      if (rollDiceHandler != null) {
        rollDiceHandler.run();
      }
    });

    newGameButton.setOnAction(e -> {
      if (newGameHandler != null) {
        newGameHandler.run();
      }
    });

    loadButton.setOnAction(e -> {
      if (loadGameHandler != null) {
        loadGameHandler.run();
      }
    });

    controls = new HBox(15, menuButton, rollButton, diceView, newGameButton, loadButton);
    controls.setAlignment(Pos.CENTER);
    controls.setPadding(new Insets(15, 0, 0, 0));

    root.setBottom(controls);

    // Add root to main stack
    mainStack.getChildren().add(root);

    // Create and add Clippy notification (positioned bottom right)
    clippyNotification = new ClippyNotification();
    mainStack.getChildren().add(clippyNotification);
    StackPane.setAlignment(clippyNotification, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(clippyNotification, new Insets(0, 20, 20, 0));
  }

  /**
   * Toggles the visibility of the settings panel in the game view.
   * If the settings panel is currently visible, it is hidden and the game controls are re-enabled.
   * If the settings panel is currently hidden, it becomes visible and the game controls are disabled.
   */
  private void toggleSettings() {
    if (settingsVisible) {
      hideSettings();
    } else {
      showSettings();
    }
  }

  /**
   * Displays the settings panel and adjusts the state of the application accordingly.
   */
  private void showSettings() {
    settingsPanel.updateDiceCount(model.getDice().getNumberOfDice());
    settingsPanel.setVisible(true);
    settingsPanel.setManaged(true);
    settingsVisible = true;
    settingsButton.setText("Hide Settings");

    // Disable game controls while settings are open
    rollButton.setDisable(true);
    newGameButton.setDisable(true);
    loadButton.setDisable(true);
  }

  /**
   * Hides the settings panel in the game view.
   */
  private void hideSettings() {
    settingsPanel.setVisible(false);
    settingsPanel.setManaged(false);
    settingsVisible = false;
    settingsButton.setText("Settings");

    // Re-enable game controls
    rollButton.setDisable(false);
    newGameButton.setDisable(false);
    loadButton.setDisable(false);
  }

  /**
   * Updates the dice view to reflect the specified number of dice.
   * Removes the current dice view, creates a new one with the provided dice count,
   * and re-adds it to the appropriate position in the control panel.
   * If the settings panel is currently visible, it updates the displayed dice count as well.
   *
   * @param diceCount the number of dice to display in the dice view
   */
  @Override
  public void updateDiceView(int diceCount) {
    // Remove old dice view
    controls.getChildren().remove(diceView);

    // Create new dice view
    diceView = new DiceView(diceCount);

    // Add back at the same position (index 1)
    controls.getChildren().add(1, diceView);

    // Update settings panel if visible
    if (settingsVisible) {
      settingsPanel.updateDiceCount(diceCount);
    }
  }

  /**
   * Renders the game board by creating a new visual representation of it and placing it in the game view.
   * Removes the currently displayed board view, if any, and updates the view to display the new board.
   * Additionally, places all players on their respective current tile positions.
   *
   * @param board The game board to be rendered. Contains the configuration and state of the tiles that
   *              make up the board.
   */
  @Override
  public void renderBoard(Board board) {
    centerStack.getChildren().remove(boardView);
    boardView = new BoardView(board);
    centerStack.getChildren().add(0, boardView); // Add at index 0 to keep it behind settings

    // Place all players at their current positions
    for (Player player : model.getPlayers()) {
      if (player.getCurrentTile() != null) {
        updatePlayerPos(player, player.getCurrentTile().getTileId());
      }
    }
  }

  /**
   * Updates the list of players displayed in the view. Reconstructs the
   * player information view with the updated list of players, ensuring
   * that the board view remains synchronized with the player positions
   * and statuses.
   *
   * @param players the list of players whose details are to be displayed.
   *                This list contains player data including their names,
   *                current positions on the board, and associated token types.
   */
  @Override
  public void updatePlayersList(List<Player> players) {
    playerInfoView = new PlayerInfoView(players, boardView);
    root.setRight(playerInfoView);
  }

  /**
   * Sets the handler to be invoked whenever the dice count is changed.
   * This method allows external logic to be executed in response to updates
   * in the dice count within the board game view.
   *
   * @param handler A {@code Consumer<Integer>} instance that processes the new
   *                dice count. The integer parameter represents the updated
   *                number of dice.
   */
  public void setDiceCountChangeHandler(Consumer<Integer> handler) {
    this.diceCountChangeHandler = handler;
  }

  /**
   * Moves a player from an old position to a new position on the board.
   * Updates both the board view and the player information view to reflect the move.
   *
   * @param player       The {@code Player} object representing the player being moved.
   *                     Contains details such as the player's name, token type, and current game state.
   * @param oldPosition  The player's previous position on the board before the move.
   *                     This is used for updating the game state or handling animations.
   * @param newPosition  The player's new position on the board after the move.
   *                     This updates the player's current position in the game.
   */
  @Override
  public void movePlayer(Player player, int oldPosition, int newPosition) {
    boardView.updatePlayerPos(player, newPosition);
    playerInfoView.updatePlayerInfo(boardView);
  }

  /**
   * Moves a player from an old position to a new position on the board while performing an animation.
   * The player's new position is reflected in both the board view and player information view.
   * Optionally, a callback function can be executed upon completion of the animation.
   *
   * @param player       The {@code Player} object representing the player to be moved. It contains
   *                     details such as the player's name, token type, and current game state.
   * @param oldPosition  The player's previous position on the board before the move. This position
   *                     is used to track and manage animations or game state transitions.
   * @param newPosition  The player's new position on the board after the move. This updates the
   *                     player's current position in the game's visual and logical state.
   * @param onComplete   A {@code Runnable} callback that is invoked upon completion of the animation.
   *                     This allows additional actions or logic to be executed after the move.
   */
  @Override
  public void movePlayerWithAnimation(Player player, int oldPosition, int newPosition, Runnable onComplete) {
    rollButton.setDisable(true);

    boardView.animatePlayerMove(player, newPosition, () -> {
      playerInfoView.updatePlayerInfo(boardView);
      rollButton.setDisable(false);

      if (onComplete != null) {
        onComplete.run();
      }
    });
  }

  /**
   * Updates the position of the specified player on the board view.
   *
   * @param player   The {@code Player} object representing the player whose position is being updated.
   *                 Contains details such as the player's name, token type, and current game state.
   * @param position The new position of the player on the board. Represents the updated tile or spot
   *                 the player is now occupying.
   */
  public void updatePlayerPos(Player player, int position) {
    boardView.updatePlayerPos(player, position);
  }

  /**
   * Displays the result of a dice roll for a player, updating the dice view and status label
   * based on the roll result. If specific dice values are provided, those will be displayed;
   * otherwise, the single roll value is shown.
   *
   * @param player     The {@code Player} who rolled the dice. Contains the player's name,
   *                   relevant for updating the status label.
   * @param roll       The numeric result of the dice roll, used to set the default display
   *                   if no specific dice values are provided.
   * @param diceValues An array of integers representing individual dice values. If this parameter
   *                   is non-null, it overrides the single roll value for display purposes.
   */
  @Override
  public void showDiceRoll(Player player, int roll, int[] diceValues) {
    if (diceValues != null) {
      diceView.setValues(diceValues);
    } else {
      diceView.setValues(new int[]{roll});
    }
    diceView.roll();

    statusLabel.setText(player.getName() + " rolled a " + roll);
  }

  /**
   * Displays the result of a dice roll for a player using the default dice view and roll result.
   * This method is a simplified version of the overloaded {@code showDiceRoll} method,
   * which uses additional arguments for custom dice values.
   *
   * @param player The {@code Player} who rolled the dice. Contains details such as the player's name,
   *               used to update the player-specific status or notifications.
   * @param roll   The numeric result of the dice roll to be displayed.
   */
  @Override
  public void showDiceRoll(Player player, int roll) {
    showDiceRoll(player, roll, null);
  }

  /**
   * Displays the action performed by a player during the game on the status label.
   * Updates the status label to show the player's name and a description of the action,
   * such as climbing a ladder, sliding down a chute, or skipping the next turn.
   *
   * @param player The Player who performed the action. Contains the player's name and other details.
   * @param action The specific TileAction that the player has triggered. Determines the type of action
   *               to be described (e.g., LadderAction for climbing or sliding, SkipAction for skipping a turn).
   */
  @Override
  public void showAction(Player player, TileAction action) {
    String actionDesc = "special action";
    if (action != null) {
      if (action instanceof LadderAction) {
        LadderAction ladderAction = (LadderAction) action;
        actionDesc = ladderAction.isLadder() ? "climbs up a ladder" : "slides down a chute";
      } else if (action.getClass().getSimpleName().contains("Skip")) {
        actionDesc = "will skip next turn";
      }
    }

    statusLabel.setText(player.getName() + " " + actionDesc);
  }

  /**
   * Displays a player's action with an accompanying animation, updating the status label and animating the player's
   * movement on the board. Once completed, optionally triggers a callback for additional actions.
   *
   * @param player            The {@code Player} performing the action. Contains details such as the player's name
   *                          and game state.
   * @param action            The specific {@code TileAction} triggered by the player. Defines the type of action
   *                          performed (e.g., LadderAction for climbing or sliding).
   * @param destinationTileId The ID of the destination tile to which the player will move. Used in the animation
   *                          process to update the player's position.
   * @param onComplete        A {@code Runnable} callback to execute additional logic after the animation finishes.
   *                          May be null if no post-animation action is required.
   */
  @Override
  public void showActionWithAnimation(Player player, TileAction action, int destinationTileId, Runnable onComplete) {
    rollButton.setDisable(true);

    String actionDesc = "special action";
    if (action != null) {
      if (action instanceof LadderAction) {
        LadderAction ladderAction = (LadderAction) action;
        actionDesc = ladderAction.isLadder() ? "climbs up a ladder" : "slides down a chute";
      } else if (action.getClass().getSimpleName().contains("Skip")) {
        actionDesc = "will skip next turn";
      }
    }

    statusLabel.setText(player.getName() + " " + actionDesc);

    PauseTransition pause = new PauseTransition(Duration.millis(500));
    pause.setOnFinished(event -> {
      boardView.animatePlayerMove(player, destinationTileId, () -> {
        playerInfoView.updatePlayerInfo(boardView);
        rollButton.setDisable(false);

        if (onComplete != null) {
          onComplete.run();
        }
      });
    });

    pause.play();
  }


  /**
   * Highlights the current player by updating the game status label and player information view.
   *
   * @param player The current player whose turn is being highlighted.
   */
  @Override
  public void highlightCurrentPlayer(Player player) {
    statusLabel.setText(player.getName() + "'s turn");
    playerInfoView.updatePlayerInfo(boardView);
  }

  /**
   * Displays the game over message and notifies the winner.
   *
   * @param winner The Player object representing the winner of the game.
   */
  @Override
  public void showGameOver(Player winner) {
    statusLabel.setText("Game Over! " + winner.getName() + " wins!");
    rollButton.setDisable(true);

    clippyNotification.showNotification("Game Over!",
        "🎉 " + winner.getName() + " has won the game! 🎉");
  }

  /**
   * Displays an error notification.
   *
   * @param title   the title of the error notification
   * @param message the message to be displayed in the error notification
   */
  @Override
  public void showError(String title, String message) {

    Platform.runLater(() -> {
      clippyNotification.showNotification("❌ " + title, message);
    });
  }

  /**
   * Displays a notification message with the specified title and content.
   *
   * @param title   the title of the notification to be displayed
   * @param message the content of the notification to be displayed
   */
  @Override
  public void showMessage(String title, String message) {

    Platform.runLater(() -> {
      clippyNotification.showNotification("ℹ️ " + title, message);
    });
  }

  /**
   * Displays a file chooser dialog to allow the user to select a JSON file for loading a game.
   * The dialog is pre-configured to show only files with a ".json" extension, and the initial
   * directory is set to the "boards" directory within the resources folder of the application.
   *
   * @return The absolute path of the selected JSON file if the user selects a file, or null if the dialog is closed without selecting a file.
   */
  @Override
  public String showLoadDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load Game");
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("JSON Files", "*.json")
    );
    fileChooser.setInitialDirectory(
      new File(System.getProperty("user.dir") + "/src/main/resources/boards")
    );

    File file = fileChooser.showOpenDialog(mainStack.getScene().getWindow());
    return file != null ? file.getAbsolutePath() : null;
  }

  @Override
  public void setReturnToMenuHandler(Runnable handler) {
    this.returnToMenu = handler;
  }

  @Override
  public void setRollDiceHandler(Runnable handler) {
    this.rollDiceHandler = handler;
  }

  @Override
  public void setNewGameHandler(Runnable handler) {
    this.newGameHandler = handler;
  }

  @Override
  public void setLoadGameHandler(Runnable handler) {
    this.loadGameHandler = handler;
  }


  public Parent getRoot() {
    return mainStack; // Return main stack instead of root BorderPane
  }
}