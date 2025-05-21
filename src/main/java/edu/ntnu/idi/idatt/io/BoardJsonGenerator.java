package edu.ntnu.idi.idatt.io;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;

public class BoardJsonGenerator {
  public static void generateStandardBoard(String outputFilePath) throws IOException {
    JsonObject boardJson = new JsonObject();
    boardJson.addProperty("rows", 10);
    boardJson.addProperty("columns", 10);

    JsonArray tilesArray = new JsonArray();


    for (int id = 1; id <= 100; id++) {
      int row = 9 - ((id - 1) / 10);
      int col = (row % 2 == 1) ? ((id - 1) % 10) : (9 - ((id - 1) % 10));

      JsonObject tileJson = new JsonObject();
      tileJson.addProperty("id", id);
      tileJson.addProperty("row", row);
      tileJson.addProperty("col", col);


      if (id < 100) {
        tileJson.addProperty("nextTile", id + 1);
      }

      tilesArray.add(tileJson);
    }

    addAction(tilesArray, 4, 14);
    addAction(tilesArray, 9, 31);
    addAction(tilesArray, 20, 38);
    addAction(tilesArray, 28, 84);
    addAction(tilesArray, 40, 59);
    addAction(tilesArray, 51, 67);
    addAction(tilesArray, 63, 81);

    addAction(tilesArray, 17, 7);
    addAction(tilesArray, 54, 34);
    addAction(tilesArray, 62, 19);
    addAction(tilesArray, 64, 60);
    addAction(tilesArray, 87, 24);
    addAction(tilesArray, 93, 73);
    addAction(tilesArray, 95, 75);
    addAction(tilesArray, 99, 78);

    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(outputFilePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    }
  }

  private static void addAction(JsonArray tilesArray, int tileId, int destinationId) {
    for (JsonElement element : tilesArray) {
      JsonObject tile = element.getAsJsonObject();
      if (tile.get("id").getAsInt() == tileId) {
        JsonObject action = new JsonObject();
        action.addProperty("type", "LadderAction");
        action.addProperty("destinationTileId", destinationId);
        tile.add("action", action);
        break;
      }
    }
  }
  public static void generateSmallBoard(String outputFilePath) throws IOException {
    JsonObject boardJson = new JsonObject();
    boardJson.addProperty("rows", 6);
    boardJson.addProperty("columns", 6);

    JsonArray tilesArray = new JsonArray();

    for (int id = 1; id <= 36; id++) {
      int row = 5 - ((id - 1) / 6);
      int col = (row % 2 == 1) ? ((id - 1) % 6) : (5 - ((id - 1) % 6));

      JsonObject tileJson = new JsonObject();
      tileJson.addProperty("id", id);
      tileJson.addProperty("row", row);
      tileJson.addProperty("col", col);

      if (id < 36) { // Hvis det ikke er den siste ruten
        tileJson.addProperty("nextTile", id + 1);
      }

      tilesArray.add(tileJson);
    }


    addAction(tilesArray, 3, 12);
    addAction(tilesArray, 8, 18);
    addAction(tilesArray, 22, 5);


    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(outputFilePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    }
  }
  public static void generateLargeBoard(String outputFilePath) throws IOException {
    JsonObject boardJson = new JsonObject();
    boardJson.addProperty("rows", 12);
    boardJson.addProperty("columns", 12);

    JsonArray tilesArray = new JsonArray();

    for (int id = 1; id <= 144; id++) {
      int row = 11 - ((id - 1) / 12);
      int col = (row % 2 == 1) ? ((id - 1) % 12) : (11 - ((id - 1) % 12));

      JsonObject tileJson = new JsonObject();
      tileJson.addProperty("id", id);
      tileJson.addProperty("row", row);
      tileJson.addProperty("col", col);

      if (id < 144) {
        tileJson.addProperty("nextTile", id + 1);
      }

      tilesArray.add(tileJson);
    }


    addAction(tilesArray, 5, 25);
    addAction(tilesArray, 40, 60);
    addAction(tilesArray, 100, 120);
    addAction(tilesArray, 121, 50);
    addAction(tilesArray, 136, 111);

    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(outputFilePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    }
  }



  public static void main(String[] args) {
    try {
      generateLargeBoard("src/main/resources/large_board.json");
      System.out.println("Board file generated successfully!");
    } catch (IOException e) {
      System.err.println("Error generating board: " + e.getMessage());
    }
  }

}