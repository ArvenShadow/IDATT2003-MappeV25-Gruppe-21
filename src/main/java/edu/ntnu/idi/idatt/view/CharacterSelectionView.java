package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.controller.CharacterSelectionController;
import edu.ntnu.idi.idatt.model.PlayerData;
import edu.ntnu.idi.idatt.view.components.PlayerPanel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

/**
 * The CharacterSelectionView class is responsible for constructing and managing
 * the user interface for selecting players and their tokens in the game. It provides
 * functionalities for adding player details, managing file operations like saving
 * and loading player configurations, and navigating between screens.
 */
public class CharacterSelectionView {
  private BorderPane root;
  private List<PlayerPanel> playerPanels;
  private List<String> availableTokens;

  // Handlers
  private Runnable startGameHandler;
  private Runnable backHandler;
  private Runnable savePlayersHandler;
  private Runnable loadPlayersHandler;

  private CharacterSelectionController controller;

  /**
   * Constructs a new instance of the CharacterSelectionView class.
   * This constructor initializes the character selection view.
   * This class serves as the primary view component for managing the process of
   * selecting player characters in the application.
   */
  public CharacterSelectionView() {
    initializeTokens();
    createUI();

    // Create controller after UI is ready
    this.controller = new CharacterSelectionController(this);
  }

  /**
   * Initializes the list of available player tokens for the application.
   * The tokens represent distinct options that can be selected by players.
   * This method populates the `availableTokens` list with predefined token names.
   * Tokens include "TopHat," "RaceCar," "Shoe," "Thimble," and "Cat."
   */
  private void initializeTokens() {
    availableTokens = new ArrayList<>();
    availableTokens.add("TopHat");
    availableTokens.add("RaceCar");
    availableTokens.add("Shoe");
    availableTokens.add("Thimble");
    availableTokens.add("Cat");
  }

  /**
   * Constructs and initializes the user interface for character selection.
   * This method assembles various UI components, including a title label,
   * player configuration panels, and buttons for file operations and navigation.
   */
  private void createUI() {
    root = new BorderPane();
    root.setPadding(new Insets(20));
    root.getStyleClass().add("character-selection");

    VBox contentBox = new VBox(20);
    contentBox.setAlignment(Pos.CENTER);

    // Title
    Label titleLabel = new Label("Select Players");
    titleLabel.setFont(javafx.scene.text.Font.font("Arial", 24));
    titleLabel.getStyleClass().add("title-label");

    // Create player panels
    GridPane playersGrid = createPlayerPanels();

    // Create button sections
    HBox fileOperationBox = createFileOperationButtons();
    HBox navigationBox = createNavigationButtons();

    contentBox.getChildren().addAll(titleLabel, playersGrid, fileOperationBox, navigationBox);
    root.setCenter(contentBox);
  }

  /**
   * Creates and initializes a GridPane containing player panels for setting up players.
   * Each player panel represents a graphical interface for configuring a player.
   * By default, the first two player panels are active, while the remaining two are inactive.
   *
   * @return A GridPane containing the player panels arranged in a 2x2 grid.
   */
  private GridPane createPlayerPanels() {
    playerPanels = new ArrayList<>();
    GridPane playersGrid = new GridPane();
    playersGrid.setHgap(20);
    playersGrid.setVgap(20);
    playersGrid.setAlignment(Pos.CENTER);

    // Create 4 player panels (2 active by default)
    for (int i = 1; i <= 4; i++) {
      PlayerPanel panel = new PlayerPanel(i, i <= 2, availableTokens);
      playerPanels.add(panel);

      int row = (i - 1) / 2;
      int col = (i - 1) % 2;
      playersGrid.add(panel.getPane(), col, row);
    }

    return playersGrid;
  }

  /**
   * Creates a horizontal box containing buttons for saving and loading player data.
   *
   * @return an HBox containing the save and load buttons for file operations
   */

  private HBox createFileOperationButtons() {
    Button savePlayersButton = new Button("Save Players");
    Button loadPlayersButton = new Button("Load Players");

    savePlayersButton.setOnAction(e -> {
      if (savePlayersHandler != null) {
        savePlayersHandler.run();
      }
    });

    loadPlayersButton.setOnAction(e -> {
      if (loadPlayersHandler != null) {
        loadPlayersHandler.run();
      }
    });

    HBox fileOperationBox = new HBox(15, savePlayersButton, loadPlayersButton);
    fileOperationBox.setAlignment(Pos.CENTER);
    return fileOperationBox;
  }

  /**
   * Creates a horizontal box containing navigation buttons for the user interface.
   * The box includes a "Back" button that triggers the backHandler action,
   * and a "Start Game" button that triggers the startGameHandler action if validation succeeds.
   * The buttons are aligned centrally within the box.
   *
   * @return an HBox containing the navigation buttons ("Back" and "Start Game")
   */
  private HBox createNavigationButtons() {
    Button backButton = new Button("Back");
    Button startButton = new Button("Start Game");

    backButton.setOnAction(e -> {
      if (backHandler != null) {
        backHandler.run();
      }
    });

    startButton.setOnAction(e -> {
      if (startGameHandler != null && validateAndStart()) {
        startGameHandler.run();
      }
    });

    HBox navigationBox = new HBox(20, backButton, startButton);
    navigationBox.setAlignment(Pos.CENTER);
    navigationBox.setPadding(new Insets(20, 0, 0, 0));
    return navigationBox;
  }

  /**
   * Validates the selected players and triggers the start game process if validation succeeds.
   *
   * @return true if the selected players pass all validation checks, false otherwise
   */
  private boolean validateAndStart() {
    List<PlayerData> selectedPlayers = getSelectedPlayers();
    return controller.validatePlayerSelection(selectedPlayers);
  }

  /**
   * Retrieves a list of active players based on the current data in the player panels.
   * An active player is defined as a panel that has valid data, including a non-empty
   * name and a selected token.
   *
   * @return a list of PlayerData objects representing active players. Each PlayerData
   *         object contains the name and selected token of a valid player.
   */
  public List<PlayerData> getActivePlayers() {
    List<PlayerData> activePlayers = new ArrayList<>();
    for (PlayerPanel panel : playerPanels) {
      if (panel.hasValidData()) {
        PlayerData data = new PlayerData();
        data.setName(panel.getPlayerName().trim());
        data.setToken(panel.getSelectedToken());
        activePlayers.add(data);
      }
    }
    return activePlayers;
  }

  /**
   * Retrieves a list of selected players configured in the player panels.
   * A player is included in the list if their corresponding panel is active.
   * Each selected player is represented by a PlayerData object, which contains
   * the player's name and chosen token.
   *
   * @return a list of PlayerData objects representing the selected players.
   */
  public List<PlayerData> getSelectedPlayers() {
    List<PlayerData> selectedPlayers = new ArrayList<>();
    for (PlayerPanel panel : playerPanels) {
      if (panel.isActive()) {
        PlayerData data = new PlayerData();
        data.setName(panel.getPlayerName());
        data.setToken(panel.getSelectedToken());
        selectedPlayers.add(data);
      }
    }
    return selectedPlayers;
  }

  /**
   * Updates the state of the player panels based on the provided list of player data.
   * This method resets all player panels to their default state and then updates
   * each panel with the corresponding data from the list, activating and assigning
   * the player's name and token as specified.
   *
   * @param playerDataList A list of PlayerData objects representing the players
   *                       whose information will be used to update the player panels.
   */
  public void updatePlayersFromData(List<PlayerData> playerDataList) {
    // Reset all panels
    for (PlayerPanel panel : playerPanels) {
      panel.setActive(false);
      panel.clearData();
    }

    // Populate with loaded data
    int panelIndex = 0;
    for (PlayerData data : playerDataList) {
      if (panelIndex < playerPanels.size()) {
        PlayerPanel panel = playerPanels.get(panelIndex);
        panel.setActive(true);
        panel.setPlayerName(data.getName());
        panel.setSelectedToken(data.getToken());
        panelIndex++;
      }
    }
  }

  /**
   * Displays a Save File dialog to the user, allowing the user to specify the
   * location and name of a file to save data. The dialog enforces the ".csv" file
   * extension and sets a default file name and directory.
   *
   * @return the absolute path of the file chosen by the user, or null if
   *         the dialog was cancelled or no file was selected.
   */
  public String showSaveDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Players");
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    fileChooser.setInitialDirectory(new File("src/main/resources/playerFiles"));
    fileChooser.setInitialFileName("players.csv");

    File file = fileChooser.showSaveDialog(root.getScene().getWindow());
    return file != null ? file.getAbsolutePath() : null;
  }

  /**
   * Displays a "Load File" dialog to the user, allowing them to select a file for loading player data.
   * The dialog restricts the file selection to `.csv` files and opens in a predefined directory.
   *
   * @return the absolute path of the selected file if a file is chosen, or null if the dialog is cancelled.
   */

  public String showLoadDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Load Players");
    fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter("CSV Files", "*.csv")
    );
    fileChooser.setInitialDirectory(new File("src/main/resources/playerFiles"));

    File file = fileChooser.showOpenDialog(root.getScene().getWindow());
    return file != null ? file.getAbsolutePath() : null;
  }

  /**
   * Displays an alert dialog with the specified title, message, and alert type.
   * The alert type determines the visual style and icon of the dialog.
   *
   * @param title the title of the alert dialog
   * @param message the content message displayed in the alert dialog
   * @param alertType the type of the alert, e.g., "WARNING", "ERROR", "INFORMATION"
   */
  public void showAlert(String title, String message, String alertType) {
    Alert.AlertType type = switch (alertType) {
      case "WARNING" -> Alert.AlertType.WARNING;
      case "ERROR" -> Alert.AlertType.ERROR;
      case "INFORMATION" -> Alert.AlertType.INFORMATION;
      default -> Alert.AlertType.NONE;
    };

    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  // Handler setters
  public void setStartGameHandler(Runnable handler) {
    this.startGameHandler = handler;
  }

  public void setBackHandler(Runnable handler) {
    this.backHandler = handler;
  }

  public void setSavePlayersHandler(Runnable handler) {
    this.savePlayersHandler = handler;
  }

  public void setLoadPlayersHandler(Runnable handler) {
    this.loadPlayersHandler = handler;
  }

  public Parent getRoot() {
    return root;
  }
}