package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.action.ChuteAction;
import edu.ntnu.idi.idatt.action.LadderAction;
import edu.ntnu.idi.idatt.dice.Dice;
import edu.ntnu.idi.idatt.event.BoardGameObserver;
import edu.ntnu.idi.idatt.event.GameEvent;
import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.factory.BoardGameFactory;
import edu.ntnu.idi.idatt.io.BoardJsonHandler;

import java.util.ArrayList;
import java.util.List;


public class BoardGame {
  private Board board;
  private Player currentPlayer;
  private final List<Player> players = new ArrayList<>();
  private Dice dice;
  private boolean gameFinished = false;
  private Player winner = null;
  private final int finalTileId = 100;

  private List<BoardGameObserver> observers = new ArrayList<>();

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void createBoard(int rows, int cols) {
    this.board = new Board(rows, cols);
    this.board.setupGameBoard();

    setupLaddersAndChutes();
  }

  private void setupLaddersAndChutes() {
    Tile ladder = board.getTile(4);
    if (ladder != null) {
      ladder.setTileAction(new LadderAction(14));
    }
    Tile chute = board.getTile(17);
    if (chute != null) {
      chute.setTileAction(new ChuteAction(7));
    }
  }

  public void playOneRound() {
    if (gameFinished) {
      return;
    }

    for (Player player : players) {
      if (gameFinished) {
        break;
      }

      currentPlayer = player;
      int roll = dice.Roll();
      try {
        Tile oldTile = player.getCurrentTile();
        player.move(roll);
        if (player.hasWon(finalTileId)) {
          gameFinished = true;
          winner = player;
          System.out.println(player.getName() + " has won! Everyone else sucks!");

          notifyObservers(new GameEvent(GameEvent.EventType.GAME_OVER, player, oldTile, player.getCurrentTile()));
        }

      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public boolean isFinished() {
    return gameFinished;
  }

  public Player getWinner() {
    return winner;
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

  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(GameEvent event) {
    for (BoardGameObserver observer : observers) {
      observer.update(event);
    }
  }

  public void createBoard() {
    this.board = BoardGameFactory.createBoard();
  }

  public void saveBoardToFile(String filename) throws BoardGameException {
    BoardJsonHandler handler = new BoardJsonHandler();
    handler.writeToFile(this.board, filename);
  }

  public void loadBoardFromFile(String filename) throws BoardGameException {
    BoardJsonHandler handler = new BoardJsonHandler();
    this.board = handler.readFromFile(filename);
  }
}
