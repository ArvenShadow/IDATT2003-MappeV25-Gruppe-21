package edu.ntnu.idi.idatt.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

class BoardTest {
  private Board board;

  @BeforeEach
  void setUp() {
    board = new Board(5, 5); // Small board for testing
  }

  @Test
  @DisplayName("Constructor initializes board with correct dimensions")
  void testConstructor() {
    assertEquals(5, board.getNumRows());
    assertEquals(5, board.getNumCols());
  }

  @Test
  @DisplayName("setupGameBoard() creates correct number of tiles")
  void testSetupGameBoardTileCount() {
    board.setupGameBoard();

    Map<Integer, Tile> tiles = board.getAllTiles();
    assertEquals(25, tiles.size());

    // Verify all tile IDs exist
    for (int i = 1; i <= 25; i++) {
      assertNotNull(board.getTile(i), "Tile " + i + " should exist");
    }
  }

  @Test
  @DisplayName("setupGameBoard() creates tiles with correct positions")
  void testSetupGameBoardPositions() {
    board.setupGameBoard();

    // Test corners
    Tile tile1 = board.getTile(1);
    assertEquals(4, tile1.getRow()); // Bottom row
    assertEquals(0, tile1.getCol()); // Left column

    Tile tile5 = board.getTile(5);
    assertEquals(4, tile5.getRow());
    assertEquals(4, tile5.getCol()); // Right column

    // Test snake pattern - row 3 (second row) should go right to left
    Tile tile6 = board.getTile(6);
    assertEquals(3, tile6.getRow());
    assertEquals(4, tile6.getCol()); // Starts from right

    Tile tile10 = board.getTile(10);
    assertEquals(3, tile10.getRow());
    assertEquals(0, tile10.getCol()); // Ends at left
  }

  @Test
  @DisplayName("Snake pattern links tiles correctly")
  void testSnakePatternLinks() {
    board.setupGameBoard();

    // First row goes left to right
    for (int i = 1; i < 5; i++) {
      Tile current = board.getTile(i);
      Tile next = board.getTile(i + 1);
      assertSame(next, current.getNextTile());
    }

    // Transition from row to row
    Tile tile5 = board.getTile(5);
    Tile tile6 = board.getTile(6);
    assertSame(tile6, tile5.getNextTile());

    // Last tile has no next
    Tile lastTile = board.getTile(25);
    assertNull(lastTile.getNextTile());
  }

  @Test
  @DisplayName("addTile() adds tiles correctly")
  void testAddTile() {
    Tile customTile = new Tile(99, 0, 0);
    board.addTile(customTile);

    assertSame(customTile, board.getTile(99));
  }

  @Test
  @DisplayName("getTile() returns null for non-existent tiles")
  void testGetTileNonExistent() {
    assertNull(board.getTile(0));
    assertNull(board.getTile(-1));
    assertNull(board.getTile(999));
  }

  @Test
  @DisplayName("getFinalTile() returns correct tile")
  void testGetFinalTile() {
    board.setupGameBoard();

    Tile finalTile = board.getFinalTile();
    assertNotNull(finalTile);
    assertEquals(25, finalTile.getTileId());
  }

  @Test
  @DisplayName("getFinalTileId() returns correct ID")
  void testGetFinalTileId() {
    assertEquals(25, board.getFinalTileId());

    Board largerBoard = new Board(10, 10);
    assertEquals(100, largerBoard.getFinalTileId());
  }

  @Test
  @DisplayName("getAllTiles() returns defensive copy")
  void testGetAllTilesDefensiveCopy() {
    board.setupGameBoard();

    Map<Integer, Tile> tiles1 = board.getAllTiles();
    Map<Integer, Tile> tiles2 = board.getAllTiles();

    assertEquals(tiles1, tiles2);
    assertNotSame(tiles1, tiles2); // Different map instances

    // Modifying returned map shouldn't affect board
    tiles1.clear();
    assertEquals(25, board.getAllTiles().size());
  }

  @Test
  @DisplayName("Large board snake pattern works correctly")
  void testLargeBoardSnakePattern() {
    Board largeBoard = new Board(10, 10);
    largeBoard.setupGameBoard();

    // Test specific tiles for 10x10 board
    Tile tile1 = largeBoard.getTile(1);
    assertEquals(9, tile1.getRow());
    assertEquals(0, tile1.getCol());

    // Row 8 should go right to left (tiles 11-20)
    Tile tile11 = largeBoard.getTile(11);
    assertEquals(8, tile11.getRow());
    assertEquals(9, tile11.getCol()); // Rightmost

    Tile tile20 = largeBoard.getTile(20);
    assertEquals(8, tile20.getRow());
    assertEquals(0, tile20.getCol()); // Leftmost
  }
}