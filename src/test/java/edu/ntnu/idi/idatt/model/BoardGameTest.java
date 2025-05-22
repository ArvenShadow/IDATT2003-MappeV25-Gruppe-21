package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.exception.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


class BoardGameTest {
  private BoardGame game;
  private Player player1;
  private Player player2;

  @BeforeEach
  void setUp() {
    game = new BoardGame();
    game.createBoard();
    game.createDice(2);

    player1 = new Player("Alice", game, "TopHat");
    player2 = new Player("Bob", game, "RaceCar");
  }

  @Test
  @DisplayName("Constructor initializes empty game")
  void testConstructor() {
    BoardGame newGame = new BoardGame();
    assertFalse(newGame.isFinished());
    assertNull(newGame.getWinner());
    assertTrue(newGame.getPlayers().isEmpty());
  }

  @Test
  @DisplayName("createBoard() initializes board")
  void testCreateBoard() {
    BoardGame newGame = new BoardGame();
    newGame.createBoard();

    assertNotNull(newGame.getBoard());
    assertEquals(100, newGame.getBoard().getAllTiles().size());
  }

  @Test
  @DisplayName("createDice() initializes dice")
  void testCreateDice() {
    BoardGame newGame = new BoardGame();
    newGame.createDice(3);

    assertNotNull(newGame.getDice());
    assertEquals(3, newGame.getDice().getNumberOfDice());
  }

  @Test
  @DisplayName("addPlayer() adds players correctly")
  void testAddPlayer() {
    game.addPlayer(player1);
    assertEquals(1, game.getPlayers().size());
    assertTrue(game.getPlayers().contains(player1));

    game.addPlayer(player2);
    assertEquals(2, game.getPlayers().size());
  }

  @Test
  @DisplayName("addPlayer() validates null player")
  void testAddPlayerNull() {
    assertThrows(IllegalArgumentException.class,
      () -> game.addPlayer(null));
  }

  @Test
  @DisplayName("addPlayer() enforces maximum player limit")
  void testAddPlayerMaximum() {
    for (int i = 1; i <= 5; i++) {
      game.addPlayer(new Player("Player" + i, game, "Token" + i));
    }

    assertThrows(InvalidGameStateException.class,
      () -> game.addPlayer(new Player("Extra", game, "Extra")));
  }

  @Test
  @DisplayName("getCurrentPlayer() returns first player initially")
  void testGetCurrentPlayerInitial() {
    game.addPlayer(player1);
    game.addPlayer(player2);

    assertSame(player1, game.getCurrentPlayer());
  }

  @Test
  @DisplayName("getCurrentPlayer() throws when no players")
  void testGetCurrentPlayerNoPlayers() {
    assertThrows(InvalidGameStateException.class,
      () -> game.getCurrentPlayer());
  }

  @Test
  @DisplayName("playTurn() moves player correctly") //Integration testing: playTurn Method used for testing purposes
  void testPlayTurn() {
    game.addPlayer(player1);
    player1.placeOnTile(game.getBoard().getTile(1));

    game.playTurn(player1);

    // Player should have moved from tile 1
    assertNotEquals(1, player1.getCurrentTile().getTileId());
  }

  @Test
  @DisplayName("playTurn() doesn't execute when game finished")
  void testPlayTurnWhenFinished() {
    game.addPlayer(player1);
    player1.placeOnTile(game.getBoard().getTile(99));
    game.setGameFinished(true);

    int positionBefore = player1.getCurrentTile().getTileId();
    game.playTurn(player1);

    assertEquals(positionBefore, player1.getCurrentTile().getTileId());
  }

  @Test
  @DisplayName("Game detects winner correctly")
  void testWinnerDetection() {
    game.addPlayer(player1);
    player1.placeOnTile(game.getBoard().getTile(99));

    assertFalse(game.isFinished());
    assertNull(game.getWinner());

    game.playTurn(player1);

    // Player should reach tile 100 and win
    assertTrue(game.isFinished());
    assertSame(player1, game.getWinner());
  }

  @Test
  @DisplayName("advanceToNextPlayer() cycles correctly")
  void testAdvanceToNextPlayer() {
    game.addPlayer(player1);
    game.addPlayer(player2);

    assertSame(player1, game.getCurrentPlayer());

    game.advanceToNextPlayer();
    assertSame(player2, game.getCurrentPlayer());

    game.advanceToNextPlayer();
    assertSame(player1, game.getCurrentPlayer());
  }

  @Test
  @DisplayName("resetGameState() clears game state")
  void testResetGameState() {
    game.addPlayer(player1);
    game.setWinner(player1);
    game.setGameFinished(true);
    game.advanceToNextPlayer();

    game.resetGameState();

    assertFalse(game.isFinished());
    assertNull(game.getWinner());
    assertSame(player1, game.getCurrentPlayer());
  }

  @Test
  @DisplayName("loadBoardFromFile() loads board")
  void testLoadBoardFromFile() throws Exception {
    game.loadBoardFromFile("src/main/resources/standard_board.json");

    assertNotNull(game.getBoard());
    assertEquals(10, game.getBoard().getNumRows());
    assertEquals(10, game.getBoard().getNumCols());
  }

  @Test
  @DisplayName("loadBoardFromFile() throws on invalid file")
  void testLoadBoardFromInvalidFile() {
    assertThrows(Exception.class,
      () -> game.loadBoardFromFile("nonexistent.json"));
  }

  @Test
  @DisplayName("movePlayerToTile() places player correctly")
  void testMovePlayerToTile() {
    game.addPlayer(player1);
    player1.placeOnTile(game.getBoard().getTile(1));

    game.movePlayerToTile(player1, 50);

    assertEquals(50, player1.getCurrentTile().getTileId());
  }
}