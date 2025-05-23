package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.BoardGameController;
import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.PlayerData;
import edu.ntnu.idi.idatt.view.BoardGameViewImpl;
import edu.ntnu.idi.idatt.view.BoardSelectionView;
import edu.ntnu.idi.idatt.view.CharacterSelectionView;
import edu.ntnu.idi.idatt.view.MainMenuView;
import java.util.List;
import java.util.Stack;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The NavigationManager class is a Singleton class responsible for handling navigation between
 * different screens of the application. It manages the navigation history, the primary
 * stage and scene, and connects views and controllers for various game states.
 * This class implements the NavigationHandler interface.
 */
public class NavigationManager implements NavigationHandler {
  private static NavigationManager instance;

  private Stage primaryStage;
  private Scene scene;
  private Stack<NavTo> navigationHistory;

  // Game components
  private BoardGame boardGame;

  /**
   * Constructs a new instance of NavigationManager and initializes
   * the navigation history stack. This constructor is private to
   * enforce the Singleton design pattern, ensuring that only one
   * instance of NavigationManager exists throughout the application.
   */
  private NavigationManager() {
    navigationHistory = new Stack<>();
  }

  /**
   * Retrieves the instance of the NavigationManager. If the instance
   * does not already exist, it creates a new one.
   *
   * @return The instance of the NavigationManager.
   */
  public static NavigationManager getInstance() {
    if (instance == null) {
      instance = new NavigationManager();
    }
    return instance;
  }

  /**
   * Initializes the primary stage and scene for the application with specified parameters
   * and sets up the initial game model. This method configures the stage properties
   * such as title, dimensions, minimum size requirements, and enables full-screen mode.
   * Additionally, a new instance of the game model {@code BoardGame} is created.
   *
   * @param stage The primary stage of the JavaFX application. This Stage object acts as the
   *              top-level container for the UI.
   * @param title The title of the application window, displayed in the stage's title bar.
   * @param width The width of the application window in pixels.
   * @param height The height of the application window in pixels.
   */
  public void initialize(Stage stage, String title, int width, int height) {
    this.primaryStage = stage;
    this.scene = new Scene(new javafx.scene.layout.StackPane(), width, height);
    primaryStage.setScene(scene);
    primaryStage.setTitle(title);
    primaryStage.setMinWidth(800);
    primaryStage.setMinHeight(600);
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");

    // Create initial game model
    this.boardGame = new BoardGame();
  }

  /**
   * Navigates to a specified screen or game state within the application.
   * The method updates the navigation history and displays the appropriate screen
   * based on the target destination provided.
   *
   * @param target The target screen or state to navigate to. This value is of the
   *               {@code NavTo} enumeration type, which includes options such as
   *               START_SCREEN, CHARACTER_SELECTION, GAME_SCREEN, LOAD_GAME_SCREEN,
   *               and BOARD_SELECTION_SCREEN.
   */
  @Override
  public void navigateTo(NavTo target) {
    navigationHistory.push(target);

    switch (target) {
      case START_SCREEN:
        showMainMenu();
        break;
      case CHARACTER_SELECTION:
        showCharacterSelection();
        break;
      case GAME_SCREEN:
        showGameScreen();
        break;
      case BOARD_SELECTION_SCREEN:
        showBoardSelectionScreen();
        break;
    }
  }

  /**
   * Navigates to the previous screen in the navigation history, if available.
   */
  @Override
  public void navigateBack() {
    if (navigationHistory.size() > 1) {
      navigationHistory.pop();
      NavTo previous = navigationHistory.pop();
      navigateTo(previous);
    }
  }

  /**
   * Sets the root node of the scene to the specified {@code Parent} object.
   * This updates the currently displayed view in the application.
   *
   * @param newRoot The new root node to be set for the scene. This {@code Parent}
   *                object represents the top-level container of the current UI hierarchy.
   */
  @Override
  public void setRoot(Parent newRoot) {
    scene.setRoot(newRoot);
  }

  /**
   * Displays the main menu screen for the application by initializing a
   * {@link MainMenuView} instance and configuring its event handlers for
   * navigation and exit actions. This method sets the main menu as the root
   * view of the application's scene.
   */
  private void showMainMenu() {
    MainMenuView mainMenuView = new MainMenuView();
    mainMenuView.setNewGameHandler(() -> navigateTo(NavTo.BOARD_SELECTION_SCREEN));
    //mainMenuView.setLoadGameHandler(() -> navigateTo(NavTo.LOAD_GAME_SCREEN));
    mainMenuView.setExitHandler(() -> primaryStage.close());

    setRoot(mainMenuView.getRoot());
  }

  /**
   * Displays the character selection screen within the application.
   * This method initializes any required components for character selection and
   * sets up event handlers for starting the game or navigating back to the previous screen.
   */
  private void showCharacterSelection() {
    // Initialize a new game
    //boardGame = new BoardGame();
    //boardGame.createBoard();
    boardGame.createDice(2);

    CharacterSelectionView characterSelectionView = new CharacterSelectionView();
    characterSelectionView.setStartGameHandler(() -> {
      // Add selected players to the game
      List<PlayerData> selectedPlayers = characterSelectionView.getSelectedPlayers();
      for (PlayerData data : selectedPlayers) {
        Player player = new Player(data.getName(), boardGame, data.getToken());
        boardGame.addPlayer(player);
        player.placeOnTile(boardGame.getBoard().getTile(1));
      }

      navigateTo(NavTo.GAME_SCREEN);
    });
    characterSelectionView.setBackHandler(() -> navigateBack());

    setRoot(characterSelectionView.getRoot());
  }

  /**
   * Displays the game screen by initializing the required view and controller components.
   * This method creates an instance of {@code BoardGameViewImpl} using the board game model
   * as its data source. It then connects the view to a {@code BoardGameController}, which
   * facilitates interaction between the model and the view. Once initialized, the view's
   * root node is set as the current root of the application scene, effectively transitioning
   * to the game screen.
   */
  private void showGameScreen() {
    // Create the actual implementation of BoardGameView
    BoardGameViewImpl gameView = new BoardGameViewImpl(boardGame);

    // Connect view with controller
    BoardGameController controller = new BoardGameController(boardGame, gameView);

    // Set the view as root
    setRoot(gameView.getRoot());
  }



  private void showBoardSelectionScreen() {
    BoardSelectionView view = new BoardSelectionView();
    setRoot(view.getRoot());
  }

  /**
   * Selects a game board from the specified file path and proceeds to the
   * character selection screen.
   *
   * @param filepath The path to the file containing the board configuration.
   *                 This should be a valid path to a JSON file describing
   *                 the board setup.
   */
  public void selectBoardAndContinue(String filepath) {
    try {
      boardGame.loadBoardFromFile(filepath);

      navigateTo(NavTo.CHARACTER_SELECTION);
    } catch (Exception e) {
      System.err.println("Error loading board: " + e.getMessage());
    }
  }   //This was generated with help by ChatGPT for aid with debugging (hence the language mismatch)
      //EDIT: Since been patched
}