package edu.ntnu.idi.idatt.action;


import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;

/**
 * Represents an action to move a player via a ladder or chute
 * to a specified destination tile. The direction of movement
 * (up the ladder or down the chute) is determined based on the
 * destination tile relative to the player's current tile.
 */
public class LadderAction implements TileAction {

  private final int destinationTileId;
  private boolean isLadder; //True for up, false for down

  /**
   * Constructs a LadderAction that moves a player to a specified destination tile.
   * The nature of the movement (ladder or chute) will depend on the context of its execution.
   *
   * @param destinationTileId the ID of the tile to which the player will be moved by this action
   */
  public LadderAction(int destinationTileId) {
    this.destinationTileId = destinationTileId;
    this.isLadder = true;
  }

  /**
   * Executes the action of moving a player via a ladder or chute to a specified destination tile.
   * Determines whether the movement is up a ladder
   * or down a chute based on the destination tile's ID
   * in relation to the player's current tile.
   *
   * @param player The player object to be moved. Represents the player performing the action.
   *               Cannot be null and must have a current tile and an associated game.
   */
  @Override
  public void perform(Player player) {
    int startTileId = player.getCurrentTile().getTileId();
    isLadder = destinationTileId > startTileId;

    String actionType = isLadder ? "climbs ladder" : "slides down chute";
    System.out.println(player.getName() + " " + actionType + " to tile " + destinationTileId);

    Tile destinationTile = player.getGame().getBoard().getTile(destinationTileId);
    if (destinationTile != null) {
      Tile oldTile = player.getCurrentTile();
      player.placeOnTile(destinationTile);

      // Notify observers if game supports it
      BoardGame game = player.getGame();
    }
  }

  /**
   * Retrieves the ID of the destination tile associated with this action.
   *
   * @return
   * the ID of the destination tile that the player will move to when this action is performed
   *
   */

  public int getDestinationTileId() {
    return destinationTileId;
  }

  /**
   * Checks whether the action involves ascending a ladder.
   *
   * @return true if the action represents moving up a ladder, false if it represents sliding down a chute
   */

  public boolean isLadder() {
    return isLadder;
  }

}



