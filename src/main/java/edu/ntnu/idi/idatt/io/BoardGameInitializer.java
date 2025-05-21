package edu.ntnu.idi.idatt.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.model.Board;

import java.io.File;
import java.util.Scanner;

public class BoardGameInitializer {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("Choose an option: ");
        System.out.println("1. Use an existing board");
        System.out.println("2. Make a custom board");
        int choice = scan.nextInt();

        switch (choice) {
            case 1:
                loadExistingBoard();
                break;
            case 2:
                createCustomBoard();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
    private static void loadExistingBoard(){
        File[] boardFiles = BoardFileSelector.getBoardFiles("src/main/resources/");
        System.out.println("Available boards: ");
        for(int i = 0; i < boardFiles.length; i++) {
            System.out.println((i + 1) + ": " + boardFiles[i].getName().replace(".json", ""));

    }
        Scanner scan = new Scanner(System.in);
        System.out.println("\nEnter the board number to load: ");
        int boardChoice = scan.nextInt() - 1;

        if(boardChoice >= 0 && boardChoice < boardFiles.length){
            String filename = boardFiles[boardChoice].getAbsolutePath();
            BoardJsonHandler handler = new BoardJsonHandler();
            try {
                Board board = handler.readFromFile(filename);
                System.out.println("Board loaded successfully! " + filename);
            } catch (BoardGameException e) {
                System.err.println("Failed to load board: " + e.getMessage());
            }
        } else{
            System.out.println("Invalid choice");
        }
    }

    private static void createCustomBoard(){
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter the number of rows: ");
        int numRows = scan.nextInt();

        System.out.println("Enter the number of columns: ");
        int numCols = scan.nextInt();

        JsonObject JsonBoard = new JsonObject();
        JsonBoard.addProperty("numRows: ", numRows);
        JsonBoard.addProperty("numCols: ", numCols);

        JsonArray tilesArray = new JsonArray();
        for(int i = 1; i <= numRows * numCols; i++){
            JsonObject tile = new JsonObject();
            tile.addProperty("id", i);


            int row = numRows - 1 - ((i-1) / numCols);
            int col = (row % 2 == 1) ? ((i-1) % numCols) : (numCols - ((i-1) % numCols));
            tile.addProperty("row", row);
            tile.addProperty("col", col);

            if(i < numRows * numCols){
                tile.addProperty("nextTile", i + 1);
            }

            tilesArray.add(tile);
        }

        System.out.println("\nAdd chutes and ladders");
        boolean moreActions = true;
        while(moreActions){
            System.out.println("Enter start-tile id: (0 to stop adding actions)");
            int startTileId = scan.nextInt();
            if(startTileId == 0){
                moreActions = false;
            } else{
                System.out.println("Enter end-tile id: ");
                int endTileId = scan.nextInt();

                String actionType = startTileId < endTileId ? "LadderAction" : "ChuteAction";

                for(JsonElement element : tilesArray){
                    JsonObject tile = element.getAsJsonObject();
                    if(tile.get("id").getAsInt() == startTileId){
                    }
                }

            }
        }












    }
}
