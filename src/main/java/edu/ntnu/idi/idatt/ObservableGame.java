package edu.ntnu.idi.idatt;

public interface ObservableGame {
  void addObserver(BoardGameObserver observer);
  void removeObserver(BoardGameObserver observer);
  void notifyObservers(GameEvent event);
}
