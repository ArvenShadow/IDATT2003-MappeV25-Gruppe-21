package edu.ntnu.idi.idatt.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PlayerTest {
  @Mock
  private BoardGame mockGame;
  @Mock
  private Board mockBoard;

  private Player player;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    when(mockGame.getBoard()).thenReturn(mockBoard);
    player = new Player("TestPlayer", mockGame, "TopHat");
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  @DisplayName("Constructor with valid parameters")
  void testValidConstructor() {
    assertEquals("TestPlayer", player.getName());
    assertEquals("TopHat", player.getTokenType());
    assertSame(mockGame, player.getGame());
    assertNull(player.getCurrentTile());
  }

  @Test
  @DisplayName("Constructor validates null name")
  void testConstructorNullName() {
    assertThrows(IllegalArgumentException.class,
      () -> new Player(null, mockGame, "TopHat"));
  }

  @Test
  @DisplayName("Constructor validates empty name")
  void testConstructorEmptyName() {
    assertThrows(IllegalArgumentException.class,
      () -> new Player("", mockGame, "TopHat"));
    assertThrows(IllegalArgumentException.class,
      () -> new Player("   ", mockGame, "TopHat"));
  }

  @Test
  @DisplayName("Constructor validates null game")
  void testConstructorNullGame() {
    assertThrows(IllegalArgumentException.class,
      () -> new Player("TestPlayer", null, "TopHat"));
  }

  @Test
  @DisplayName("Constructor validates null token")
  void testConstructorNullToken() {
    assertThrows(IllegalArgumentException.class,
      () -> new Player("TestPlayer", mockGame, null));
  }

  @Test
  @DisplayName("Constructor trims player name")
  void testConstructorTrimsName() {
    Player playerWithSpaces = new Player("  TestPlayer  ", mockGame, "TopHat");
    assertEquals("TestPlayer", playerWithSpaces.getName());
  }

  @Test
  @DisplayName("placeOnTile() sets current tile")
  void testPlaceOnTile() {
    Tile tile = new Tile(5, 0, 4);
    player.placeOnTile(tile);

    assertSame(tile, player.getCurrentTile());
  }

  @Test
  @DisplayName("move() with valid steps")
  void testMoveValidSteps() {
    Tile startTile = new Tile(1, 0, 0);
    Tile targetTile = new Tile(4, 0, 3);

    player.placeOnTile(startTile);
    when(mockBoard.getTile(4)).thenReturn(targetTile);
    when(mockBoard.getFinalTileId()).thenReturn(100);

    player.move(3);

    assertSame(targetTile, player.getCurrentTile());
  }

  @Test
  @DisplayName("move() throws exception when not on board")
  void testMoveWhenNotOnBoard() {
    assertThrows(IllegalStateException.class, () -> player.move(3));
  }

  @Test
  @DisplayName("move() handles movement beyond final tile")
  void testMoveBeyondFinalTile() {
    Tile startTile = new Tile(98, 0, 1);
    Tile finalTile = new Tile(100, 0, 9);

    player.placeOnTile(startTile);
    when(mockBoard.getFinalTileId()).thenReturn(100);
    when(mockBoard.getTile(100)).thenReturn(finalTile);

    player.move(5); // Would go to 103, but should stop at 100

    assertSame(finalTile, player.getCurrentTile());
  }

  @Test
  @DisplayName("move() executes tile action")
  void testMoveExecutesTileAction() {
    Tile startTile = new Tile(1, 0, 0);
    Tile targetTile = mock(Tile.class);
    when(targetTile.getTileId()).thenReturn(4);

    player.placeOnTile(startTile);
    when(mockBoard.getTile(4)).thenReturn(targetTile);
    when(mockBoard.getFinalTileId()).thenReturn(100);

    player.move(3);

    verify(targetTile).landAction(player);
  }

  @Test
  @DisplayName("hasWon() returns true when at or past final tile")
  void testHasWon() {
    Tile finalTile = new Tile(100, 9, 9);
    player.placeOnTile(finalTile);

    assertTrue(player.hasWon(100));
    assertTrue(player.hasWon(99)); // Past final
  }

  @Test
  @DisplayName("hasWon() returns false when not at final tile")
  void testHasNotWon() {
    Tile currentTile = new Tile(50, 4, 9);
    player.placeOnTile(currentTile);

    assertFalse(player.hasWon(100));
  }

  @Test
  @DisplayName("hasWon() returns false when not on board")
  void testHasWonWhenNotOnBoard() {
    assertFalse(player.hasWon(100));
  }

  @Test
  @DisplayName("Skip turn functionality")
  void testSkipTurnFunctionality() {
    assertFalse(player.getSkipsNextTurn());

    player.setSkipsNextTurn(true);
    assertTrue(player.getSkipsNextTurn());

    player.setSkipsNextTurn(false);
    assertFalse(player.getSkipsNextTurn());
  }
}