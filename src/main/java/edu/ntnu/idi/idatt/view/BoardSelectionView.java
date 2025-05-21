package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.io.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.Board;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLOutput;

public class BoardSelectionView {
    public BorderPane getMainMenuLayout() {

        Button smallBoardButton = new Button("Small board (5x5)");
        Button standardBoardButton = new Button("Normal board (8x8)");
        Button largeBoardButton = new Button("Large board (10x10)");
        Button exitButton = new Button("Exit");

        smallBoardButton.getStyleClass().add("button");
        standardBoardButton.getStyleClass().add("button");
        largeBoardButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("exit-button");

        HBox bottomRow = new HBox(20);
        bottomRow.getChildren().addAll(smallBoardButton, largeBoardButton);
        bottomRow.setAlignment(Pos.CENTER);

        VBox centerLayout = new VBox(15);
        centerLayout.getChildren().addAll(standardBoardButton, bottomRow);
        centerLayout.setAlignment(Pos.CENTER);

        centerLayout.setPadding(new Insets(20, 20, 50, 20));


        BorderPane layout = new BorderPane();
        layout.setCenter(centerLayout);
        layout.setBottom(exitButton);
        BorderPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(exitButton, new Insets(10));


        smallBoardButton.setOnAction(e -> {
            System.out.println("Small board selected");
        });
        smallBoardButton.setOnAction(e -> {selectBoard("src/main/resources/small_board.json");
        });

        standardBoardButton.setOnAction(e -> {
            System.out.println("Normal board selected");
        });
        standardBoardButton.setOnAction(e -> {selectBoard("src/main/resources/standard_board.json");
        });

        largeBoardButton.setOnAction(e -> {
            System.out.println("Large board selected");
        });
        largeBoardButton.setOnAction(e -> {selectBoard("src/main/resources/large_board.json");
        });
        exitButton.setOnAction(e -> {
            System.out.println("Exiting program...");
            System.exit(0);
        });

        return layout;
    }
    public Scene getStyledScene() {
        BorderPane layout = getMainMenuLayout();
        layout.getStyleClass().add("main-menu");

        Scene scene = new Scene(layout, 800, 600);

        try {
            String css = getClass().getResource("/styles/Select_board.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (NullPointerException e) {
            System.err.println("Could not find CSS file: /styles/Select_board.css");
            e.printStackTrace();
        }

        return scene;
    }

    private void selectBoard(String filepath){
        System.out.println("Loading board from: " + filepath);

        try{
            BoardJsonHandler handler = new BoardJsonHandler();
            Board board = handler.readFromFile(filepath);
            System.out.println("Board loaded successfully! with rows: " + board.getNumRows()
                    + " and columns: " + board.getNumCols());
        } catch (Exception e){
            System.err.println("Failed to load board: " + e.getMessage());
        }
    }
}

