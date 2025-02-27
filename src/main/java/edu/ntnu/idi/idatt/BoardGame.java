package edu.ntnu.idi.idatt;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
  private Board board;
  private Player currentPlayer;
  private List<Player> players;
  private Dice dice;

  public BoardGame() {
    this.board = new Board();
    this.players = new ArrayList<>();
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard() {
    for (int i = 1; i <= 100; i++) {
      Tile tile = new Tile(i);
      if (i == 5) {
        tile.setTileAction(new LadderAction(10));
      } else if (i == 11) {
        tile.setTileAction(new ChuteAction(6));
      }
      board.addTile(tile);
    }
  }

  public void createDice() {
    this.dice = new Dice(1);
  }

  public Board getBoard() {
    return board;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Dice getDice() {
    return dice;
  }

}
