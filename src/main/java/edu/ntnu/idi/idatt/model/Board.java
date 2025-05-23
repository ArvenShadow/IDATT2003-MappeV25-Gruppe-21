package edu.ntnu.idi.idatt.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game board consisting of a grid of tiles. Each tile is uniquely identified by an ID
 * and can be linked to another tile in a snake-like pattern. The board supports operations to add
 * tiles, retrieve tiles by their ID, and configure the game grid.
 */
public class Board {
  private final Map<Integer, Tile> tiles;
  private int numRows;
  private int numCols;

  /**
   * Constructs a game board with the specified number of rows and columns.
   * Initializes the board to be an empty grid of tiles.
   *
   * @param numRows The number of rows in the board.
   * @param numCols The number of columns in the board.
   */
  public Board(int numRows, int numCols) {
    this.tiles = new HashMap<>();
    this.numRows = numRows;
    this.numCols = numCols;
  }

  /**
   * Sets up the game board by creating and populating the grid of tiles, adding each tile
   * to the board's internal storage, and establishing a snake-like linking pattern for the tiles.
   * This method ensures that the game board is initialized with all tiles properly placed
   * and linked. The tiles are assigned IDs based on their grid positions, taking into account
   * the desired snake-like order.
   */
  public void setupGameBoard() {
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        int tileId = calculateTileId(r, c);
        Tile tile = new Tile(tileId, r, c);
        addTile(tile);
      }
    }
    snakePatternTiles();
  }

  /**
   * Calculates the unique ID of a tile based on its row and column in a game board.
   * The ID assignment accounts for a "snake-like" traversal pattern where alternate rows
   * are ordered in reverse.
   *
   * @param row The row index of the tile (0-based, starting from the top).
   * @param col The column index of the tile (0-based, starting from the left).
   * @return The unique numeric ID of the specified tile.
   */
  private int calculateTileId(int row, int col) {
    int rowFromBottom = numRows - 1 - row;
    if (rowFromBottom % 2 == 0) {
      return rowFromBottom * numCols + col + 1;
    } else {
      return rowFromBottom * numCols + (numCols - col);
    }
  }

  /**
   * Establishes a snake-like linking pattern between tiles on the board by setting
   * the `nextTile` property of each tile to the subsequent tile in the pattern. For
   * every tile on the board, this method retrieves the current tile and the next tile,
   * then links them sequentially. The last tile will not have a `nextTile` set.
   * Used to create a traversal path across tiles, enabling a sequential navigation
   * from the first tile to the last tile on the board.
   */
  private void snakePatternTiles() {
    for (int i = 1; i < numRows * numCols; i++) {
      Tile current = getTile(i);
      Tile next = getTile(i + 1);
      if (current != null && next != null) {
        current.setNextTile(next);
      }
    }
  }

  /**
   * Adds a tile to the board by storing it in the internal data structure with its unique ID as the key.
   *
   * @param tile The tile to be added to the board.
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Retrieves the tile associated with the specified tile ID.
   *
   * @param tileId The unique identifier for the tile to retrieve.
   * @return The Tile object corresponding to the given tile ID, or null if no tile is found with the specified ID.
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Retrieves the number of rows in the board.
   *
   * @return The total number of rows in the board.
   */
  public int getNumRows() {
    return numRows;
  }

  /**
   * Retrieves the number of columns in the board.
   *
   * @return The total number of columns in the board.
   */
  public int getNumCols() {
    return numCols;
  }

  /**
   * Gets the final tile in the board.
   *
   * @return The final tile (with highest ID)
   */
  public Tile getFinalTile() {
    int maxId = numRows * numCols;
    return tiles.get(maxId);
  }

  /**
   * Gets the ID of the final tile.
   *
   * @return The ID of the final tile
   */
  public int getFinalTileId() {
    return numRows * numCols;
  }

  /**
   * Gets all tiles in the board.
   *
   * @return Map of all tiles with their IDs
   */
  public Map<Integer, Tile> getAllTiles() {
    return new HashMap<>(tiles);
  }

}