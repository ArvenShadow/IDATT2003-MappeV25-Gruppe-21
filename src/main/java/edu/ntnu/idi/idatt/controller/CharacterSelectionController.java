package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.io.PlayerCsvHandler;
import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.PlayerData;
import edu.ntnu.idi.idatt.view.CharacterSelectionView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller class responsible for managing character selection in a board game application.
 * This class interacts between the view layer for character selection and CSV file operations
 * for saving/loading player data.
 */

public class CharacterSelectionController {
  private CharacterSelectionView view;
  private PlayerCsvHandler csvHandler;

  /**
   * Creates a new instance of the CharacterSelectionController
   * and initializes it with the given view.
   * The controller sets up event handlers for saving and loading player data.
   *
   * @param view the CharacterSelectionView instance that this controller is associated with
   */

  public CharacterSelectionController(CharacterSelectionView view) {
    this.view = view;
    this.csvHandler = new PlayerCsvHandler(new BoardGame()); // Temporary game for CSV operations

    // Set up view event handlers
    view.setSavePlayersHandler(this::handleSavePlayers);
    view.setLoadPlayersHandler(this::handleLoadPlayers);
  }

  /**
   * Handles the saving of active players to a CSV file. This method retrieves the list of
   * active players from the view and prompts the user to specify a file location for saving.
   * If there are no active players, an alert is displayed, and the method exits.
   */

  private void handleSavePlayers() {
    try {
      List<PlayerData> activePlayers = view.getActivePlayers();

      if (activePlayers.isEmpty()) {
        view.showAlert("No Players", "No active players to save.", "WARNING");
        return;
      }

      String filename = view.showSaveDialog();
      if (filename != null) {
        List<Player> players = convertToPlayers(activePlayers);
        csvHandler.writeToFile(players, filename);

        String fileName = new File(filename).getName();
        view.showAlert("Success",
              "Players saved successfully to " + fileName, "INFORMATION");
      }

    } catch (BoardGameException e) {
      view.showAlert("Save Error",
          "Failed to save players: " + e.getMessage(), "ERROR");
    } catch (Exception e) {
      view.showAlert("Unexpected Error",
          "An unexpected error occurred: " + e.getMessage(), "ERROR");
    }
  }

  /**
   * Handles the loading of player data from a CSV file. This method opens a file selection dialog
   * to allow the user to choose a file containing player data. If a file is selected, the method
   * reads the data from the file, converts it into a format suitable for the UI, and updates the
   * view with the loaded player information.
   */

  private void handleLoadPlayers() {
    try {
      String filename = view.showLoadDialog();
      if (filename != null) {
        List<Player> loadedPlayers = csvHandler.readFromFile(filename);
        List<PlayerData> playerDataList = convertToPlayerData(loadedPlayers);

        view.updatePlayersFromData(playerDataList);

        String fileName = new File(filename).getName();
      }

    } catch (BoardGameException e) {
      view.showAlert("Load Error",
            "Failed to load players: " + e.getMessage(), "ERROR");
    } catch (Exception e) {
      view.showAlert("Unexpected Error",
            "An unexpected error occurred: " + e.getMessage(), "ERROR");
    }
  }

  /**
   * Validates the player selection criteria, ensuring that at least two players are selected,
   * each player has a non-empty name, each player has selected a token, and all tokens are unique.
   * If any validation fails, a warning alert is displayed and the method returns false.
   *
   * @param players the list of PlayerData objects representing the selected players
   * @return true if all validation checks pass, false otherwise
   */

  public boolean validatePlayerSelection(List<PlayerData> players) {
    if (players.size() < 2) {
      view.showAlert("Invalid Selection",
          "At least 2 players are required.", "WARNING");
      return false;
    }

    // Check for empty names
    for (int i = 0; i < players.size(); i++) {
      PlayerData player = players.get(i);
      if (player.getName() == null || player.getName().trim().isEmpty()) {
        view.showAlert("Invalid Selection",
            "Player " + (i + 1) + " needs a name.", "WARNING");
        return false;
      }
      if (player.getToken() == null) {
        view.showAlert("Invalid Selection",
            "Player " + (i + 1) + " needs to select a token.", "WARNING");
        return false;
      }
    }

    // Check for duplicate tokens
    Set<String> usedTokens = new HashSet<>();
    for (PlayerData player : players) {
      if (usedTokens.contains(player.getToken())) {
        view.showAlert("Invalid Selection",
            "Each player must have a unique token.", "WARNING");
        return false;
      }
      usedTokens.add(player.getToken());
    }

    return true;
  }

  /**
   * Converts a list of PlayerData objects into a list of Player objects.
   * Each PlayerData object is mapped to a Player instance, with default attributes
   * for the game object and token type.
   *
   * @param playerDataList the list of PlayerData objects to be converted
   * @return a list of Player objects created from the given PlayerData list
   */

  private List<Player> convertToPlayers(List<PlayerData> playerDataList) {
    BoardGame tempGame = new BoardGame();
    List<Player> players = new ArrayList<>();
    for (PlayerData data : playerDataList) {
      players.add(new Player(data.getName(), tempGame, data.getToken()));
    }
    return players;
  }

  /**
   * Converts a list of Player objects into a list of PlayerData objects.
   * Each Player object's name and token type are mapped to a corresponding PlayerData object.
   *
   * @param players the list of Player objects to be converted
   * @return a list of PlayerData objects created from the given Player list
   */

  private List<PlayerData> convertToPlayerData(List<Player> players) {
    List<PlayerData> playerDataList = new ArrayList<>();
    for (Player player : players) {
      PlayerData data = new PlayerData();
      data.setName(player.getName());
      data.setToken(player.getTokenType());
      playerDataList.add(data);
    }
    return playerDataList;
  }
}