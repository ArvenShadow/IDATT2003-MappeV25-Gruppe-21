package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.action.TileAction;
import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.exception.InvalidGameStateException;
import edu.ntnu.idi.idatt.io.BoardJsonHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a board game that includes a playing board, dice, and players.
 * The class provides functionality to manage the board state, player turns,
 * dice rolls, and game mechanics such as determining the winner and resetting the game state.
 */

public class BoardGame {
  private Board board;
  private Dice dice;
  private List<Player> players;
  private int currentPlayerIndex;
  private boolean gameFinished;
  private Player winner;


  /**
   * Constructs a new instance of the BoardGame class.
   * Initializes the player list, sets the current player index to zero,
   * and marks the game as not finished.
   */
  public BoardGame() {
    this.players = new ArrayList<>();
    this.currentPlayerIndex = 0;
    this.gameFinished = false;
  }

  /**
   * Creates and initializes the game board for the board game instance.
   * This method uses the `BoardGameFactory.createBoard` method to generate
   * a new board.
   *
   * @throws RuntimeException if an error occurs during the creation of the board,
   *                          including issues in reading or processing the board
   *                          configuration file.
   */
  public void createBoard() {
    try {
      this.board = edu.ntnu.idi.idatt.factory.BoardGameFactory.createBoard();

    } catch (RuntimeException e) {
      // Log error or handle exception
      System.err.println("Error creating board: " + e.getMessage());
      throw e;
    }
  }

  /**
   * Loads the game board from a specified file and initializes the board state.
   * This method utilizes the `BoardJsonHandler` to deserialize the board from
   * a JSON file into a `Board` object. The deserialized board is then set
   * as the current board for the game.
   *
   * @param filepath The path to the file containing the board configuration
   *                 in JSON format. It should include the file name and
   *                 directory path if applicable.
   * @throws Exception If an error occurs during file reading or the board
   *                   configuration is invalid (e.g., invalid JSON format,
   *                   missing required fields, or I/O errors).
   */
  public void loadBoardFromFile(String filepath) throws Exception {
    BoardJsonHandler boardHandler = new BoardJsonHandler();
    this.board = boardHandler.readFromFile(filepath);

  }

  public void createDice(int numberOfDice) {
    this.dice = new Dice(numberOfDice);
  }

  /**
   * Executes a single turn for the specified player in the board game.
   * This method is not in use by the game itself, rather functions as a method for
   * integration testing between classes.
   *
   * @param player The player whose turn is being executed. The player object represents
   *               the current participant in the game and includes their current position,
   *               token type, and game state.
   */
  public void playTurn(Player player) {   //Method for testing purposes
    if (gameFinished) {
      return;
    }

    int[] diceValues = dice.rollAllDice();
    int totalRoll = dice.getTotal();

    int oldPosition = player.getCurrentTile().getTileId();

    // Calculate new position
    int newPosition = oldPosition + totalRoll;
    Tile targetTile = board.getTile(newPosition);

    if (targetTile == null) {
      // Handle case where player would move beyond the board
      targetTile = board.getFinalTile();
    }

    player.placeOnTile(targetTile);


    // Apply tile action if any
    if (targetTile.getTileAction() != null) {
      TileAction action = targetTile.getTileAction();
      action.perform(player);
    }

    // Check if player has won
    if (player.hasWon(board.getFinalTileId())) {
      winner = player;
      gameFinished = true;
    }

    // Move to next player
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

  }

  /**
   * Moves a player to a specific tile by ID.
   */
  public void movePlayerToTile(Player player, int tileId) {
    Tile tile = board.getTile(tileId);
    if (tile != null) {
      player.placeOnTile(tile);
    }
  }

  /**
   * Advances to the next player.
   */
  public void advanceToNextPlayer() {
    if (players.isEmpty()) {
      return;
    }
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  /**
   * Sets the game's winner.
   */
  public void setWinner(Player player) {
    this.winner = player;
  }

  /**
   * Sets the game's finished state.
   */
  public void setGameFinished(boolean finished) {
    this.gameFinished = finished;
    if (finished && winner != null) {
    }
  }

  /**
   * Resets the current player index to 0.
   */
  public void resetCurrentPlayerIndex() {
    this.currentPlayerIndex = 0;
  }

  /**
   * Resets the entire game state for a new game.
   */
  public void resetGameState() {
    this.gameFinished = false;
    this.winner = null;
    this.currentPlayerIndex = 0;
  }

  public boolean isFinished() {
    return gameFinished;
  }

  public Player getWinner() {
    return winner;
  }

  public Board getBoard() {
    return board;
  }

  public Dice getDice() {
    return dice;
  }

  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Retrieves the player whose turn it currently is.
   *
   * @return The current player, represented as a {@code Player} object.
   * @throws InvalidGameStateException if there are no players in the game.
   */
  public Player getCurrentPlayer() {
    if (players.isEmpty()) {
      throw new InvalidGameStateException("No players in the game");
    }
    return players.get(currentPlayerIndex);
  }

  public void addPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (players.size() >= 5) {
      throw new InvalidGameStateException("Maximum 5 players allowed");
    }
    players.add(player);
  }


  /**
   * Loads a saved game from the specified file. This method initializes the game
   * board from the file, resets the game state, and places players in their
   * initial positions or restores their saved positions.
   *
   * @param filename The name of the file containing the saved game data. The file
   *                 should exist and be properly formatted for the game to load
   *                 successfully.
   * @throws BoardGameException If an error occurs while loading the game, including
   *                            issues such as file not found, invalid file format,
   *                            or corrupted data.
   */

  public void loadGame(String filename) throws BoardGameException {
    try {
      BoardJsonHandler boardHandler = new BoardJsonHandler();
      this.board = boardHandler.readFromFile(filename);

      // Reset game state
      resetGameState();

      // Place players at start position or restore their positions
      // For simplicity in this implementation, just place them at start
      for (Player player : players) {
        player.placeOnTile(board.getTile(1));
      }


    } catch (Exception e) {
      throw new BoardGameException("Failed to load game: " + e.getMessage(), e);
    }
  }
}