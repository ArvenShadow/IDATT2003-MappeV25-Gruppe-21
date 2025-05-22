package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.action.TileAction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TileTest {
  @Mock
  private TileAction mockAction;
  @Mock
  private Player mockPlayer;

  private Tile tile;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    tile = new Tile(42, 4, 2);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  @DisplayName("Constructor sets properties correctly")
  void testConstructor() {
    assertEquals(42, tile.getTileId());
    assertEquals(4, tile.getRow());
    assertEquals(2, tile.getCol());
    assertNull(tile.getNextTile());
    assertNull(tile.getTileAction());
  }

  @Test
  @DisplayName("setNextTile() and getNextTile() work correctly")
  void testNextTile() {
    Tile nextTile = new Tile(43, 4, 3);

    tile.setNextTile(nextTile);
    assertSame(nextTile, tile.getNextTile());

    tile.setNextTile(null);
    assertNull(tile.getNextTile());
  }

  @Test
  @DisplayName("setTileAction() and getTileAction() work correctly")
  void testTileAction() {
    tile.setTileAction(mockAction);
    assertSame(mockAction, tile.getTileAction());

    tile.setTileAction(null);
    assertNull(tile.getTileAction());
  }

  @Test
  @DisplayName("landAction() executes action when present")
  void testLandActionWithAction() {
    tile.setTileAction(mockAction);

    tile.landAction(mockPlayer);

    verify(mockAction, times(1)).perform(mockPlayer);
  }

  @Test
  @DisplayName("landAction() does nothing when no action")
  void testLandActionWithoutAction() {
    assertDoesNotThrow(() -> tile.landAction(mockPlayer));
  }

  @Test
  @DisplayName("landAction() handles action exceptions gracefully")
  void testLandActionHandlesException() {
    doThrow(new RuntimeException("Test exception")).when(mockAction).perform(any());
    tile.setTileAction(mockAction);

    // Should not throw, just print error
    assertDoesNotThrow(() -> tile.landAction(mockPlayer));

    verify(mockAction, times(1)).perform(mockPlayer);
  }
}