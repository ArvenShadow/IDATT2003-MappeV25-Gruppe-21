package edu.ntnu.idi.idatt.action;

import edu.ntnu.idi.idatt.model.Player;

/**
 * Represents an action that can be performed by a player on a specific tile of the board.
 * Implementing classes define the specific behaviors of such actions, which may include
 * altering the state of the player, moving the player to another tile, or any other game-specific logic.
 */

public interface TileAction {
  void perform(Player player);
}
