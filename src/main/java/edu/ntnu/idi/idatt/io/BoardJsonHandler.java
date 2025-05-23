package edu.ntnu.idi.idatt.io;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import edu.ntnu.idi.idatt.action.LadderAction;
import edu.ntnu.idi.idatt.action.SkipTurnAction;
import edu.ntnu.idi.idatt.action.TileAction;
import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.exception.InvalidBoardConfigurationException;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Tile;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles reading from and writing to JSON files for the {@link Board} object.
 * This class implements file handling and serialization logic specific to
 * board state persistence.
 */
public class BoardJsonHandler implements FileHandler<Board> {

  @Override
  public Board readFromFile(String fileName) throws BoardGameException {
    try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
      JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
      return parseBoard(json);

    } catch (JsonSyntaxException e) {
      throw new InvalidBoardConfigurationException("Invalid JSON format in file: " + fileName, e);
    } catch (IOException e) {
      throw new BoardGameException("Cannot read board file: " + fileName, e);
    }
  }

  /**
   * Writes a Board object to a file in JSON format.
   *
   * <p>The method serializes the given Board object into a JSON representation
   * and saves it to the specified file. If there are any issues during file
   * writing (e.g., file not writable, directory does not exist), a
   * BoardGameException is thrown.
   *</p>
   *
   * @param board    The Board object to be serialized and written to the file.
   *                 It contains the configuration of tiles, rows, and columns.
   * @param filename The name of the file to which the JSON data representing
   *                 the Board object will be written. Should include the file
   *                 path if necessary.
   * @throws BoardGameException If an I/O error occurs during the writing process
   *                            or if the file cannot be created or modified.
   */
  @Override
  public void writeToFile(Board board, String filename) throws BoardGameException {
    try {
      JsonObject boardJson = serializeBoard(board);

      try (FileWriter writer = new FileWriter(filename)) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(boardJson, writer);
      }

    } catch (IOException e) {
      throw new BoardGameException("Cannot write board file: " + filename, e);
    }
  }

  private Board parseBoard(JsonObject json) throws InvalidBoardConfigurationException {
    try {
      if (!json.has("rows") || !json.has("columns") || !json.has("tiles")) {
        throw new InvalidBoardConfigurationException("Missing required fields: rows, columns, tiles");
      }

      int rows = json.get("rows").getAsInt();
      int cols = json.get("columns").getAsInt();

      if (rows <= 0 || cols <= 0) {
        throw new InvalidBoardConfigurationException("Invalid board dimensions: " + rows + "x" + cols);
      }

      Board board = new Board(rows, cols);
      JsonArray tilesArray = json.getAsJsonArray("tiles");

      for (JsonElement element : tilesArray) {
        JsonObject tileJson = element.getAsJsonObject();
        int id = tileJson.get("id").getAsInt();
        int row = tileJson.get("row").getAsInt();
        int col = tileJson.get("col").getAsInt();

        Tile tile = new Tile(id, row, col);

        if (tileJson.has("action")) {
          JsonObject actionJson = tileJson.getAsJsonObject("action");
          TileAction action = parseAction(actionJson);
          tile.setTileAction(action);
        }

        board.addTile(tile);
      }

      return board;

    } catch (NumberFormatException | IllegalStateException e) {
      throw new InvalidBoardConfigurationException("Invalid board data format", e);
    }
  }

  private TileAction parseAction(JsonObject actionJson) throws InvalidBoardConfigurationException {
    String actionType = actionJson.get("type").getAsString();

    switch (actionType) {
      case "LadderAction":
        if (!actionJson.has("destinationTileId")) {
          throw new InvalidBoardConfigurationException("LadderAction missing destinationTileId");
        }
        int destinationId = actionJson.get("destinationTileId").getAsInt();
        return new LadderAction(destinationId);

      case "SkipTurnAction":
        return new SkipTurnAction();

      default:
        return null; // Skip unknown actions
    }
  }

  private JsonObject serializeBoard(Board board) {
    JsonObject boardJson = new JsonObject();
    boardJson.addProperty("rows", board.getNumRows());
    boardJson.addProperty("columns", board.getNumCols());

    JsonArray tilesArray = new JsonArray();
    for (int tileId = 1; tileId <= board.getNumRows() * board.getNumCols(); tileId++) {
      Tile tile = board.getTile(tileId);

      if (tile != null) {
        JsonObject tileJson = new JsonObject();
        tileJson.addProperty("id", tile.getTileId());
        tileJson.addProperty("row", tile.getRow());
        tileJson.addProperty("col", tile.getCol());

        TileAction action = tile.getTileAction();
        if (action != null) {
          JsonObject actionJson = new JsonObject();

          if (action instanceof LadderAction) {
            actionJson.addProperty("type", "LadderAction");
            actionJson.addProperty("destinationTileId", ((LadderAction) action).getDestinationTileId());
          } else if (action instanceof SkipTurnAction) {
            actionJson.addProperty("type", "SkipTurnAction");
          }

          tileJson.add("action", actionJson);
        }

        tilesArray.add(tileJson);
      }
    }

    boardJson.add("tiles", tilesArray);
    return boardJson;
  }
}