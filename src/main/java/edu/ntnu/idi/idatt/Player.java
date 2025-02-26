package edu.ntnu.idi.idatt;

public class Player {
  BoardGame game;
  String name;
  Tile currentTile;

  public Player(String name, BoardGame game) {
    this.name = name;
    this.game = game;
  }

  public String getName() {
    return name;
  }

  public void placeOnTile(Tile tile) {
    currentTile = tile;
  }

  public void move(int steps) {

  }
}
