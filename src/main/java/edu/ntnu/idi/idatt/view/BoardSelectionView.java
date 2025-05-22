package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardSelectionView {

    public BoardSelectionView() {

    }

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
            NavigationManager.getInstance().selectBoardAndContinue("src/main/resources/small_board.json");
        });
        standardBoardButton.setOnAction(e -> {
            NavigationManager.getInstance().selectBoardAndContinue("src/main/resources/standard_board.json");
        });
        largeBoardButton.setOnAction(e -> {
            NavigationManager.getInstance().selectBoardAndContinue("src/main/resources/large_board.json");
        });

        exitButton.setOnAction(e -> {
            System.out.println("Exiting the program...");
            System.exit(0);
        });

        return layout;
    }

    public Parent getRoot() {
        BorderPane layout = getMainMenuLayout();
        layout.getStyleClass().add("main-menu");

        // Bruk stylesheets direkte på root-noden
        try {
            String css = getClass().getResource("/styles/Select_board.css").toExternalForm();
            if (css != null) {
                layout.getStylesheets().add(css); // dette fungerer
            }
        } catch (Exception e) {
            System.err.println("CSS ikke funnet: " + e.getMessage());
        }

        return layout;
    }
}