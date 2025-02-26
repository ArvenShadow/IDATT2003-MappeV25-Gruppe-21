package edu.ntnu.idi.idatt;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private final Map<Integer, Tile> tiles;

  public Board() {
    tiles = new HashMap<>();
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }
}