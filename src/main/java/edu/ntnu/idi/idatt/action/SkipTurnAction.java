package edu.ntnu.idi.idatt.action;

import edu.ntnu.idi.idatt.model.Player;

/**
 * Represents an action where a player is required to skip their next turn.
 * This action could be triggered, for example, by stepping on a specific game tile.
 * When executed, the player's state is updated to indicate they will skip their next turn.
 * A message is displayed to notify that the player will miss their turn.
 */
public class SkipTurnAction implements TileAction {

  /**
   * Executes the action of making a player skip their next turn.
   * Updates the player's state to indicate they should skip their
   * next turn and displays a message to notify that the player will miss their turn.
   *
   * @param player The player object whose turn will be skipped.
   *               Must not be null and must have a valid name.
   */
  @Override
  public void perform(Player player) {
    player.setSkipsNextTurn(true);
    System.out.println(player.getName() + " have to skip their next turn");

  }
}
