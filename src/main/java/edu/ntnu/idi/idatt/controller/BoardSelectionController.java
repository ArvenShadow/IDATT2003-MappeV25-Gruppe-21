package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.io.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.view.MainMenuView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BoardSelectionController {

    private final Stage primaryStage;

    public BoardSelectionController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void selectBoard(String filepath) {
        System.out.println("Loading board from " + filepath);

        try {

            BoardJsonHandler handler = new BoardJsonHandler();
            Board board = handler.readFromFile(filepath);
            System.out.println("Board loaded successfully! Rows: " + board.getNumRows() + ", Columns: " + board.getNumCols());


            navigateToMainMenu();

        } catch (Exception e) {

            System.err.println("Failed to load board: " + e.getMessage());
        }
    }


    private void navigateToMainMenu() {
        MainMenuView mainMenuView = new MainMenuView();
        Scene mainMenuScene = mainMenuView.getStyledScene();



        mainMenuView.setNewGameHandler(() -> System.out.println("New game started!"));
        mainMenuView.setLoadGameHandler(() -> System.out.println("Load game selected."));
        mainMenuView.setExitHandler(() -> {
            System.out.println("Exiting program...");
            System.exit(0);
        });


        primaryStage.setScene(mainMenuScene);
    }
}