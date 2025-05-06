package edu.ntnu.idi.idatt.io;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import edu.ntnu.idi.idatt.action.LadderAction;
import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Tile;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardJsonHandler implements FileHandler<Board> {
  @Override
  public Board readFromFile(String fileName) throws BoardGameException {
    try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
      JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();


      int rows = json.get("rows").getAsInt();
      int cols = json.get("columns").getAsInt();


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
          String actionType = actionJson.get("type").getAsString();

          if ("LadderAction".equals(actionType) && actionJson.has("destinationTileId")) {
            int destinationId = actionJson.get("destinationTileId").getAsInt();
            tile.setTileAction(new LadderAction(destinationId));
          }
        }


        board.addTile(tile);
      }


      return board;
    } catch (IOException | IllegalStateException e) {
      throw new BoardGameException("Failed to read board from file: " + e.getMessage(), e);
    }
  }

  @Override
  public void writeToFile(Board data, String filename) throws BoardGameException {
    // Eksisterende logikk for å skrive brett til fil, om nødvendig.
  }
}