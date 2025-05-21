package edu.ntnu.idi.idatt.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Scanner;

public class CreateCustomBoard {
    private static void createCustomBoard() {
        Scanner scan = new Scanner(System.in);


        System.out.println("Enter the number of rows: ");
        int numRows = scan.nextInt();

        System.out.println("Enter the number of columns: ");
        int numCols = scan.nextInt();


        JsonObject jsonBoard = new JsonObject();
        jsonBoard.addProperty("numRows", numRows);
        jsonBoard.addProperty("numCols", numCols);

        JsonArray tilesArray = new JsonArray();


        for (int i = 1; i <= numRows * numCols; i++) {
            JsonObject tile = new JsonObject();
            tile.addProperty("id", i);


            int row = numRows - 1 - ((i - 1) / numCols);
            int col = (row % 2 == 1) ? ((i - 1) % numCols) : (numCols - ((i - 1) % numCols) - 1);
            tile.addProperty("row", row);
            tile.addProperty("col", col);


            if (i < numRows * numCols) {
                tile.addProperty("nextTile", i + 1);
            }

            tilesArray.add(tile);
        }


        jsonBoard.add("tiles", tilesArray);

        System.out.println("\nAdd Chutes and Ladders:");
        boolean moreActions = true;


        while (moreActions) {
            System.out.println("Enter start-tile id: (0 to stop adding actions)");
            int startTileId = scan.nextInt();
            if (startTileId == 0) {
                moreActions = false;
            } else {
                System.out.println("Enter end-tile id: ");
                int endTileId = scan.nextInt();


                String actionType = startTileId < endTileId ? "LadderAction" : "ChuteAction";


                boolean actionAdded = false;
                for (JsonElement element : tilesArray) {
                    JsonObject tile = element.getAsJsonObject();
                    if (tile.get("id").getAsInt() == startTileId) {
                        tile.addProperty("actionType", actionType);
                        tile.addProperty("actionEnd", endTileId);
                        actionAdded = true;
                        break;
                    }
                }

                if (actionAdded) {
                    System.out.println(actionType + " added from tile " + startTileId + " to tile " + endTileId);
                } else {
                    System.out.println("Invalid start-tile id: " + startTileId);
                }
            }
        }


        System.out.println("Custom board created:");
        System.out.println(jsonBoard.toString());
    }
}
