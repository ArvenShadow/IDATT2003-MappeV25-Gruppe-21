package edu.ntnu.idi.idatt.view.ingame;

import edu.ntnu.idi.idatt.action.LadderAction;
import edu.ntnu.idi.idatt.action.SkipTurnAction;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The BoardView class is responsible for rendering a visual representation of the game board and managing
 * player interactions on the board. It extends the Pane class to provide a graphical layout for board tiles,
 * player tokens, and other visual elements such as ladders or special tiles with actions.
 *
 * AI declaration: AI helped with methods where math implementation was required. As well as the animation
 * method.
 */
public class BoardView extends Pane {
  private Board board;
  private Map<Integer, Rectangle> tileViews = new HashMap<>();
  private Map<Player, ImageView> playerTokens = new HashMap<>();
  private Map<Integer, Color> playerColors = new HashMap<>();

  private static final double TILE_SIZE = 60;
  private static final double TILE_GAP = 5;
  private static final double TOKEN_SIZE = 45; // Size for token images
  private static final Duration ANIMATION_DURATION = Duration.millis(500);

  private static final Color[] PLAYER_COLORS = {
    Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE
  };

  public BoardView(Board board) {
    this.board = board;
    createBoardLayout();
  }

  /**
   * Constructs the visual layout for the game board, including tile rectangles, text labels,
   * and indicators for special tile actions. This method processes each tile of the game board,
   * setting its position, appearance, and behavior, and adds the resulting graphical elements
   * to the visual scene of the board.
   */
  private void createBoardLayout() {
    int numRows = board.getNumRows();
    int numCols = board.getNumCols();

    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        int tileId = calculateTileId(r, c, numRows, numCols);
        Tile tile = board.getTile(tileId);

        if (tile == null) {
          continue;
        }

        double x = c * (TILE_SIZE + TILE_GAP);
        double y = r * (TILE_SIZE + TILE_GAP);

        Rectangle tileRect = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);

        if (tileId % 2 == 0) {
          tileRect.setFill(Color.WHITE);
        } else {
          tileRect.setFill(Color.LIGHTGRAY);
        }

        if (tile.getTileAction() != null) {
          if (tile.getTileAction() instanceof LadderAction) {
            LadderAction ladderAction = (LadderAction) tile.getTileAction();
            int destinationId = ladderAction.getDestinationTileId();
            System.out.println("ladderaction from " + tileId + " to " + destinationId);
            if (destinationId > tileId) {
              tileRect.setFill(Color.LIGHTGREEN);
            } else {
              tileRect.setFill(Color.LIGHTPINK);
            }
          }
        }
        tileRect.setStroke(Color.BLACK);
        tileRect.setArcHeight(10);
        tileRect.setArcWidth(10);

        Text tileText = new Text(String.valueOf(tileId));
        tileText.setX(x + TILE_SIZE / 2 - 10);
        tileText.setY(y + TILE_SIZE / 2 + 10);

        tileViews.put(tileId, tileRect);
        this.getChildren().addAll(tileRect, tileText);

        if (tile.getTileAction() != null && tile.getTileAction() instanceof SkipTurnAction) {
          createSkipTurnIndicator(x, y);
        }
      }
    }

    drawConnections();
  }

  /**
   * Draws visual connections between tiles on the board that are linked by special actions,
   * such as ladders or chutes. These connections are represented as lines between the source
   * and destination tiles.
   *
   * The method iterates through all tiles of the board and checks if a tile has a special
   * action, specifically a {@link LadderAction}. If such an action exists, a line is drawn
   * from the source tile to the destination tile determined by the action. The line's color
   * represents the direction of the action:
   * - Green for moving up (ladder).
   * - Pink for moving down (chute).
   *
   * The visual components for the connections are added to the current pane for display.
   */
  private void drawConnections() {
    for (int tileId = 1; tileId <= board.getNumRows() * board.getNumCols(); tileId++) {
      Tile tile = board.getTile(tileId);
      if (tile != null && tile.getTileAction() != null && tile.getTileAction() instanceof LadderAction) {
        LadderAction ladderAction = (LadderAction) tile.getTileAction();
        int destinationTileId = ladderAction.getDestinationTileId();

        Rectangle sourceTile = tileViews.get(tileId);
        Rectangle destinationTile = tileViews.get(destinationTileId);

        if (sourceTile != null && destinationTile != null) {
          double startX = sourceTile.getX() + TILE_SIZE / 2;
          double startY = sourceTile.getY() + TILE_SIZE / 2;
          double endX = destinationTile.getX() + TILE_SIZE / 2;
          double endY = destinationTile.getY() + TILE_SIZE / 2;

          Line connection = new Line(startX, startY, endX, endY);
          connection.setStrokeWidth(3);

          if (destinationTileId > tileId) {
            connection.setStroke(Color.LIGHTGREEN);
          } else {
            connection.setStroke(Color.LIGHTPINK);
          }

          this.getChildren().add(connection);
          //connection.toBack();
        }
      }
    }
  }

  /**
   * Calculates the tile ID based on the input row, column, number of rows, and number of columns,
   * assuming an alternating left-to-right and right-to-left numbering pattern for each row.
   *
   * @param row The row index of the tile, starting from the top of the grid.
   * @param col The column index of the tile, starting from the left of the grid.
   * @param numRows The total number of rows in the grid.
   * @param numCols The total number of columns in the grid.
   * @return The calculated tile ID that uniquely identifies the tile in the grid.
   */
  private int calculateTileId(int row, int col, int numRows, int numCols) {
    int rowFromBottom = numRows - 1 - row;
    if (rowFromBottom % 2 == 0) {
      return rowFromBottom * numCols + col + 1;
    } else {
      return rowFromBottom * numCols + (numCols - col);
    }
  }

  /**
   * Updates the position of a player's token on the game board.
   * If the player's token does not already exist, it is created and added to the board.
   * The method calculates the target position based on the specified tile ID and animates the movement
   * of the token. Player tokens are adjusted slightly to avoid overlapping if multiple players share the
   * same tile.
   *
   * @param player The player whose position is being updated. Cannot be null.
   * @param tileId The ID of the tile to which the player's token should be moved. Must correspond to a valid tile.
   */
  public void updatePlayerPos(Player player, int tileId) {
    Rectangle tileRect = tileViews.get(tileId);
    if (tileRect == null) {
      return;
    }

    ImageView playerToken = playerTokens.get(player);
    if (playerToken == null) {
      playerToken = createPlayerToken(player);
      playerTokens.put(player, playerToken);
      this.getChildren().add(playerToken);
    }

    double targetX = tileRect.getX() + TILE_SIZE / 2 - TOKEN_SIZE / 2;
    double targetY = tileRect.getY() + TILE_SIZE / 2 - TOKEN_SIZE / 2;

    int playerIndex = new ArrayList<>(playerTokens.keySet()).indexOf(player);
    targetX += (playerIndex % 2) * (TOKEN_SIZE * 0.7) - TOKEN_SIZE * 0.35;
    targetY += (playerIndex / 2) * (TOKEN_SIZE * 0.7) - TOKEN_SIZE * 0.35;

    TranslateTransition transition = new TranslateTransition(ANIMATION_DURATION, playerToken);
    transition.setToX(targetX - playerToken.getX());
    transition.setToY(targetY - playerToken.getY());
    transition.play();

    playerToken.toFront();
  }

  /**
   * Creates a visual token representation for a player. The token is displayed as an
   * {@code ImageView} on the game board. If the token's image cannot be loaded, a fallback
   * graphical representation (e.g., a colored rectangle) is used instead. The token is
   * colored based on the player's index, cycling through a predefined list of colors.
   *
   * @param player The player for whom the token is being created. Must not be null and
   *               should have a valid token type.
   * @return An {@code ImageView} representing the player's token.
   */
  private ImageView createPlayerToken(Player player) {
    int playerIndex = new ArrayList<>(playerTokens.keySet()).size();
    Color playerColor = PLAYER_COLORS[playerIndex % PLAYER_COLORS.length];
    playerColors.put(playerIndex, playerColor);

    ImageView playerToken = new ImageView();
    playerToken.setFitWidth(TOKEN_SIZE);
    playerToken.setFitHeight(TOKEN_SIZE);
    playerToken.setPreserveRatio(true);

    // Try to load token image
    String tokenType = player.getTokenType().toLowerCase();
    try {
      Image tokenImage = new Image(
        getClass().getResourceAsStream("/images/tokens/" + tokenType + ".png")
      );
      playerToken.setImage(tokenImage);
    } catch (Exception e) {
      // Fallback: create a colored circle if image can't be loaded
      System.out.println("Could not load token image for: " + tokenType + ", using fallback");

      // Create a simple colored rectangle as fallback
      Rectangle fallbackRect = new Rectangle(TOKEN_SIZE, TOKEN_SIZE, playerColor);
      fallbackRect.setStroke(Color.BLACK);
      fallbackRect.setArcWidth(5);
      fallbackRect.setArcHeight(5);

      // Convert rectangle to image and use it
      playerToken.setImage(createFallbackImage(playerColor));
    }

    return playerToken;
  }

  /**
   * Creates a fallback image when token image cannot be loaded
   */
  private Image createFallbackImage(Color color) {
    try {
      // Create a simple colored circle as fallback
      javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(TOKEN_SIZE, TOKEN_SIZE);
      javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();

      gc.setFill(color);
      gc.fillOval(2, 2, TOKEN_SIZE - 4, TOKEN_SIZE - 4);
      gc.setStroke(Color.BLACK);
      gc.setLineWidth(2);
      gc.strokeOval(2, 2, TOKEN_SIZE - 4, TOKEN_SIZE - 4);

      javafx.scene.SnapshotParameters params = new javafx.scene.SnapshotParameters();
      params.setFill(Color.TRANSPARENT);
      return canvas.snapshot(params, null);
    } catch (Exception e) {
      // If all else fails, return null and handle gracefully
      return null;
    }
  }

  /**
   * Creates a visual indicator on the board to represent a skip-turn tile.
   * The indicator consists of a red translucent circle with a dark red border
   * and two intersecting white lines (forming an X) at its center.
   * This method adds the graphical elements to the current pane.
   *
   * @param x The X-coordinate of the top-left corner of the tile where the indicator will be placed.
   * @param y The Y-coordinate of the top-left corner of the tile where the indicator will be placed.
   */
  private void createSkipTurnIndicator(double x, double y) {

    Circle blockCircle = new Circle(x + TILE_SIZE / 2, y + TILE_SIZE / 2, TILE_SIZE / 3);
    blockCircle.setFill(Color.RED);
    blockCircle.setOpacity(0.3);
    blockCircle.setStroke(Color.DARKRED);
    blockCircle.setStrokeWidth(2);

    double centerX = x + TILE_SIZE / 2;
    double centerY = y + TILE_SIZE / 2;
    double lineOffset = TILE_SIZE / 6;

    Line line1 = new Line(centerX - lineOffset, centerY - lineOffset,
      centerX + lineOffset, centerY + lineOffset
    );
    Line line2 = new Line(centerX - lineOffset, centerY + lineOffset,
      centerX + lineOffset, centerY - lineOffset
    );

    line1.setStroke(Color.WHITE);
    line1.setStrokeWidth(3);
    line2.setStroke(Color.WHITE);
    line2.setStrokeWidth(3);

    this.getChildren().addAll(blockCircle, line1, line2);
    blockCircle.toFront();
    line1.toFront();
    line2.toFront();

  }


  public Color getPlayerColor(int playerIndex) {
    return playerColors.getOrDefault(playerIndex, Color.GRAY);
  }

  /**
   * Animates the movement of a player's token to a specified tile on the game board. If the token
   * for the player does not exist, it is created and added to the board. The method calculates the
   * target position based on the tile ID, animates the movement, and ensures proper positioning of
   * tokens to avoid overlap when multiple players occupy the same tile. The token is moved to the
   * foreground during the animation.
   *
   * @param player The player whose token is being animated. Must not be null.
   * @param tileId The ID of the tile where the player's token should move. Must correspond to a valid tile.
   * @param onComplete An optional callback to be executed after the animation completes. Can be null.
   */
  public void animatePlayerMove(Player player, int tileId, Runnable onComplete) {
    Rectangle tileRect = tileViews.get(tileId);
    if (tileRect == null) {
      if (onComplete != null) onComplete.run();
      return;
    }

    ImageView playerToken = playerTokens.get(player);
    if (playerToken == null) {
      playerToken = createPlayerToken(player);
      playerTokens.put(player, playerToken);
      this.getChildren().add(playerToken);
    }

    double targetX = tileRect.getX() + TILE_SIZE / 2 - TOKEN_SIZE / 2;
    double targetY = tileRect.getY() + TILE_SIZE / 2 - TOKEN_SIZE / 2;

    int playerIndex = new ArrayList<>(playerTokens.keySet()).indexOf(player);
    targetX += (playerIndex % 2) * (TOKEN_SIZE * 0.7) - TOKEN_SIZE * 0.35;
    targetY += (playerIndex / 2) * (TOKEN_SIZE * 0.7) - TOKEN_SIZE * 0.35;

    TranslateTransition transition = new TranslateTransition(ANIMATION_DURATION, playerToken);
    transition.setToX(targetX - playerToken.getX());
    transition.setToY(targetY - playerToken.getY());

    if (onComplete != null) {
      transition.setOnFinished(event -> onComplete.run());
    }

    transition.play();

    playerToken.toFront();
  }
}