package edu.ntnu.idi.idatt.io;

import com.google.gson.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The BoardJsonGenerator class is responsible for generating board configurations
 * in JSON format for various board sizes. Each board includes tiles with properties
 * like ID, row, column, next tile, and actions such as ladders, chutes, or skip turns.
 * The generated JSON files can be saved to specified file paths.
 */
public class BoardJsonGenerator {

  /**
   * Generates a JSON representation of a standard 10x10 board game with ladders, chutes, and other actions,
   * and writes it to the specified file path.
   * AI has helped in creating these methods due to the filewriting and math not being
   * fully understood by us.
   *
   * @param outputFilePath the file path where the generated JSON board will be written
   * @throws IOException if an I/O error occurs while writing to the specified file
   */

  public static void generateStandardBoard(String outputFilePath) throws IOException {
    JsonObject boardJson = new JsonObject();
    boardJson.addProperty("rows", 10);
    boardJson.addProperty("columns", 10);

    JsonArray tilesArray = new JsonArray();

    // Generate all tiles
    // Math implemented with help of AI
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

    // Add ladder actions (going up)
    addLadderAction(tilesArray, 4, 14);
    addLadderAction(tilesArray, 9, 31);
    addLadderAction(tilesArray, 20, 38);
    addLadderAction(tilesArray, 28, 84);
    addLadderAction(tilesArray, 40, 59);
    addLadderAction(tilesArray, 51, 67);
    addLadderAction(tilesArray, 63, 81);

    // Add chute actions (going down)
    addLadderAction(tilesArray, 17, 7);
    addLadderAction(tilesArray, 54, 34);
    addLadderAction(tilesArray, 62, 19);
    addLadderAction(tilesArray, 64, 60);
    addLadderAction(tilesArray, 87, 24);
    addLadderAction(tilesArray, 93, 73);
    addLadderAction(tilesArray, 95, 75);
    addLadderAction(tilesArray, 99, 78);

    // Add skip turn actions
    addSkipTurnAction(tilesArray, 8);
    addSkipTurnAction(tilesArray, 15);
    addSkipTurnAction(tilesArray, 33);
    addSkipTurnAction(tilesArray, 47);
    addSkipTurnAction(tilesArray, 58);
    addSkipTurnAction(tilesArray, 77);
    addSkipTurnAction(tilesArray, 89);

    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(outputFilePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    }
  }

  /**
   * Adds a ladder action to a specific tile within the provided JSON array of tiles.
   * Updates the tile with the specified ID by associating a ladder action,
   * which redirects to a destination tile ID.
   *
   * @param tilesArray the JSON array containing all tile objects
   * @param tileId the ID of the tile to which the ladder action is to be added
   * @param destinationId the ID of the destination tile where the ladder leads
   */
  private static void addLadderAction(JsonArray tilesArray, int tileId, int destinationId) {
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


  /**
   * Generates a JSON representation of a small 6x6 board game with a predefined configuration
   * including rows, columns, tiles, and ladder actions, and writes it to the specified file path.
   *
   * @param outputFilePath the file path where the generated JSON board will be written
   * @throws IOException if an I/O error occurs while writing to the specified file
   */
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

      if (id < 36) {
        tileJson.addProperty("nextTile", id + 1);
      }

      tilesArray.add(tileJson);
    }

    //ladders
    addLadderAction(tilesArray, 4, 14);
    addLadderAction(tilesArray, 9, 31);
    addLadderAction(tilesArray, 20, 33);

    //chutes
    addLadderAction(tilesArray, 17, 7);
    addLadderAction(tilesArray, 23, 10);
    addLadderAction(tilesArray, 35, 2);

    //skipturns
    addSkipTurnAction(tilesArray, 8);
    addSkipTurnAction(tilesArray, 15);
    addSkipTurnAction(tilesArray, 22);


    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(outputFilePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    }
  }

  /**
   * Generates a JSON representation of a large 12x12 board game with a specific configuration,
   * including rows, columns, tile positions, and ladder actions. The generated JSON board is
   * written to the specified file path.
   *
   * @param outputFilePath the file path where the generated JSON board will be saved
   * @throws IOException if an I/O error occurs while writing to the specified file
   */
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

    // Add ladder actions (going up)
    addLadderAction(tilesArray, 4, 14);
    addLadderAction(tilesArray, 9, 31);
    addLadderAction(tilesArray, 20, 38);
    addLadderAction(tilesArray, 28, 84);
    addLadderAction(tilesArray, 40, 59);
    addLadderAction(tilesArray, 51, 67);
    addLadderAction(tilesArray, 63, 81);
    addLadderAction(tilesArray, 65, 83);
    addLadderAction(tilesArray, 78, 87);
    addLadderAction(tilesArray, 92, 120);


    // Add chute actions (going down)
    addLadderAction(tilesArray, 17, 7);
    addLadderAction(tilesArray, 54, 34);
    addLadderAction(tilesArray, 62, 19);
    addLadderAction(tilesArray, 87, 24);
    addLadderAction(tilesArray, 93, 73);
    addLadderAction(tilesArray, 99, 78);
    addLadderAction(tilesArray, 101, 79);
    addLadderAction(tilesArray, 119, 82);
    addLadderAction(tilesArray, 139, 3);

    // Add skip turn actions
    addSkipTurnAction(tilesArray, 8);
    addSkipTurnAction(tilesArray, 15);
    addSkipTurnAction(tilesArray, 33);
    addSkipTurnAction(tilesArray, 47);
    addSkipTurnAction(tilesArray, 58);
    addSkipTurnAction(tilesArray, 77);
    addSkipTurnAction(tilesArray, 89);
    addSkipTurnAction(tilesArray, 100);
    addSkipTurnAction(tilesArray, 118);
    addSkipTurnAction(tilesArray, 138);

    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(outputFilePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(boardJson, writer);
    }
  }


  /**
   * Adds a SkipTurnAction to a specific tile within the provided JSON array of tiles.
   * Updates the tile with the specified ID by associating a SkipTurnAction.
   * If the tile with the given ID is found, the action is added, and the loop terminates.
   *
   * @param tilesArray the JSON array containing all tile objects
   * @param tileId the ID of the tile to which the SkipTurnAction is to be added
   */

  private static void addSkipTurnAction(JsonArray tilesArray, int tileId) {
    for (JsonElement element : tilesArray) {
      JsonObject tile = element.getAsJsonObject();
      if (tile.get("id").getAsInt() == tileId) {
        JsonObject action = new JsonObject();
        action.addProperty("type", "SkipTurnAction");
        tile.add("action", action);
        break;
      }
    }
  }

  /**
   * The main method serves as the entry point for the application. It sequentially generates
   * board configurations of various sizes (large, small, and standard) and saves them as JSON
   * files in the specified file paths. If any IOException occurs during the file writing process,
   * an error message is displayed in the console.
   *
   * @param args an array of command-line arguments provided to the program, though not used in this implementation
   */
  public static void main(String[] args) {
    try {
      generateLargeBoard("src/main/resources/boards/large_board.json");
      System.out.println("Large Board file generated successfully!");
    } catch (IOException e) {
      System.err.println("Error generating board: " + e.getMessage());
    }

    try {
      generateSmallBoard("src/main/resources/boards/small_board.json");
      System.out.println("Small Board file generated successfully!");
    } catch (IOException e) {
      System.err.println("Error generating board: " + e.getMessage());
    }

    try {
      generateStandardBoard("src/main/resources/boards/standard_board.json");
      System.out.println("Standard Board file generated successfully!");
    } catch (IOException e) {
      System.err.println("Error generating board: " + e.getMessage());
    }
  }

}