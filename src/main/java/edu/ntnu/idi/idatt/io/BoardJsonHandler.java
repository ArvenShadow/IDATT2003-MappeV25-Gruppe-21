package edu.ntnu.idi.idatt.io;

import com.google.gson.*;
import edu.ntnu.idi.idatt.action.LadderAction;
import edu.ntnu.idi.idatt.action.TileAction;
import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Tile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BoardJsonHandler implements FileHandler<Board> {

  @Override
  public Board readFromFile(String filename) throws BoardGameException {
    try (Reader reader = new FileReader(filename)) {
      JsonObject boardJson = new JsonParser().parse(reader).getAsJsonObject();

      int rows = boardJson.get("rows").getAsInt();
      int columns = boardJson.get("columns").getAsInt();

      Board board = new Board(rows, columns);

      Map<Integer, Tile> tilesMap = new HashMap<>();
      JsonArray tilesArray = boardJson.getAsJsonArray("tiles");

      for (JsonElement tileElement : tilesArray) {
        JsonObject tileJson = tileElement.getAsJsonObject();
        int id = tileJson.get("id").getAsInt();

        int row = tileJson.get("row").getAsInt();
        int column = tileJson.get("column").getAsInt();

        Tile tile = new Tile(id, row, column);
        tilesMap.put(id, tile);
        board.addTile(tile);
      }

      for (JsonElement tileElement : tilesArray) {
        JsonObject tileJson = tileElement.getAsJsonObject();
        int id = tileJson.get("id").getAsInt();
        Tile tile = tilesMap.get(id);

        if (tileJson.has("nextTile")) {
          int nextTileId = tileJson.get("nextTile").getAsInt();
          Tile nextTile = tilesMap.get(nextTileId);
          if (nextTile != null) {
            tile.setNextTile(nextTile);
          } else {
            throw new BoardGameException("nextTile " + nextTileId + " not found");
          }
        }
        if (tileJson.has("action")) {
          JsonObject actionJson = tileJson.getAsJsonObject("action");
          String type = actionJson.get("type").getAsString();
          int destTileId = actionJson.get("destinationTileId").getAsInt();

          if ("LadderAction".equals(type)) {
            tile.setTileAction(new LadderAction(destTileId));
          }
        }
      }
      return board;
    } catch (IOException e) {
      throw new BoardGameException("Error reading board file " + filename, e);
    } catch (JsonSyntaxException e) {
      throw new BoardGameException("Invalid JSON format in board file " + filename, e);
    } catch (Exception e) {
      throw new BoardGameException("Unexpected error reading board " + filename + e.getMessage(), e);
    }
  }

  @Override
  public void writeToFile(Board board, String filename) throws BoardGameException {
    try (Writer writer = new FileWriter(filename)) {
      JsonObject boardJson = new JsonObject();
      boardJson.addProperty("rows", board.getNumRows());
      boardJson.addProperty("columns", board.getNumCols());

      JsonArray tilesArray = new JsonArray();

      for (int i = 1; i <= board.getNumRows() * board.getNumCols(); i++) {
        Tile tile = board.getTile(i);
        if (tile != null) {
          JsonObject tileJson = new JsonObject();
          tileJson.addProperty("id", tile.getTileId());
          tileJson.addProperty("row", tile.getRow());
          tileJson.addProperty("col", tile.getCol());

          Tile nextTile = tile.getNextTile();
          if (nextTile != null) {
            tileJson.addProperty("nextTile", nextTile.getTileId());
          }

          TileAction action = tile.getTileAction();
          if (action != null) {
            JsonObject actionJson = new JsonObject();

            if (action instanceof LadderAction) {
              actionJson.addProperty("type", "LadderAction");

              int destinationId = ((LadderAction) action).getDestinationTileId();

              actionJson.addProperty("destinationTileId", destinationId);
              tileJson.add("action", actionJson);
            }
          }
          tilesArray.add(tileJson);
        }
      }
      boardJson.add("tiles", tilesArray);

      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    } catch (IOException e) {
      throw new BoardGameException("Error writing board " + filename + e.getMessage(), e);
    }
  }
}
