package edu.ntnu.idi.idatt;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private final Map<Integer, Tile> tiles;
  private int numRows;
  private int numCols;

  public Board(int numRows, int numCols) {
    this.tiles = new HashMap<>();
    this.numRows = numRows;
    this.numCols = numCols;
  }

  public void setupGameBoard() {
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        int tileId = calculateTileId(r,c);
        Tile tile = new Tile(tileId,r,c);
        addTile(tile);
      }
    }
    snakePatternTiles();
  }

  private int calculateTileId(int row, int col) {
    int rowFromBottom = numRows - 1- row;
    if (rowFromBottom % 2 == 0) {
      return rowFromBottom * numCols + col + 1;
    } else {
      return rowFromBottom * numCols + (numCols - col);
    }
  }

  private void snakePatternTiles() {
    for (int i = 1; i < numRows * numCols; i++) {
      Tile current = getTile(i);
      Tile next = getTile(i + 1);
      if (current != null && next != null) {
        current.setNextTile(next);
      }
    }
  }
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public int getNumRows() {
    return numRows;
  }
  public int getNumCols() {
    return numCols;
  }
}