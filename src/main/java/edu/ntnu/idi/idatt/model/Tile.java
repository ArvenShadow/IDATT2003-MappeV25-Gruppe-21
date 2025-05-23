package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.action.TileAction;

/**
 * Represents a tile on a board game. Each tile has a unique identifier,
 * a position defined by row and column, an optional action to execute when a player lands on it,
 * and a reference to the next tile in a sequence.
 */
public class Tile {

  private int tileId;
  private Tile nextTile;
  private TileAction tileAction;
  private int row;
  private int col;

  /**
   * Constructs a Tile object with a specified tile identifier, row, and column position.
   *
   * @param tileId the unique identifier of the tile.
   * @param row the row position of the tile on the board.
   * @param col the column position of the tile on the board.
   */

  public Tile(int tileId, int row, int col) {
    this.row = row;
    this.col = col;
    this.tileId = tileId;
    this.nextTile = null;
    this.tileAction = null;
  }

  public int getTileId() {
    return tileId;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }


  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  /**
   * Sets the action associated with this tile. This action will be executed
   * when a player lands on the tile.
   *
   * @param tileAction the action to be executed for this tile. It should implement the
   *                   TileAction interface and define the specific behavior
   *                   to occur when a player lands on the tile such as climbing a ladder.
   */
  public void setTileAction(TileAction tileAction) {
    this.tileAction = tileAction;
  }

  /**
   * Executes the action associated with this tile when a player lands on it.
   * If the tile has a defined action, it attempts to perform the action using the provided player.
   * In case of errors during execution, an exception will be caught, and an error message will be printed.
   *
   * @param player the player who landed on the tile and on whom the tile's action will be performed
   */
  public void landAction(Player player) {
    if (tileAction != null) {
      try {
        tileAction.perform(player);
      } catch (Exception e) {
        System.out.println("Error performing tile action for tile " + tileId + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public TileAction getTileAction() {
    return tileAction;
  }

}
