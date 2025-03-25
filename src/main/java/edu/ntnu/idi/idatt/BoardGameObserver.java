package edu.ntnu.idi.idatt;

public interface BoardGameObserver {
    void update(GameEvent event);
}

class GameEvent {
  private EventType type;
  private Player player;
  private Tile fromTile;
  private Tile toTile;

  public GameEvent(EventType type, Player player, Tile fromTile, Tile toTile) {
    this.type = type;
    this.player = player;
    this.fromTile = fromTile;
    this.toTile = toTile;
  }

  public enum EventType {
    PLAYER_MOVED,
    LADDER_CLIMBED,
    CHUTE_SLID,
    GAME_OVER
  }
}

interface ObservableGame {
  void addObserver(BoardGameObserver observer);
  void removeObserver(BoardGameObserver observer);
  void notifyObservers(GameEvent event);
}
