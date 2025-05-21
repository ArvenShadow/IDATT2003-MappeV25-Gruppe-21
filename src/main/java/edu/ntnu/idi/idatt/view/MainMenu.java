package edu.ntnu.idi.idatt.view;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainMenu {

    public BorderPane getMainMenuLayout() {

        Button smallBoardButton = new Button("Small board (5x5)");
        Button normalBoardButton = new Button("Normal board (8x8)");
        Button largeBoardButton = new Button("Large board (10x10)");
        Button exitButton = new Button("Exit");

        smallBoardButton.getStyleClass().add("button");
        normalBoardButton.getStyleClass().add("button");
        largeBoardButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("exit-button");

        HBox bottomRow = new HBox(20);
        bottomRow.getChildren().addAll(smallBoardButton, largeBoardButton);
        bottomRow.setAlignment(Pos.CENTER);

        VBox centerLayout = new VBox(15);
        centerLayout.getChildren().addAll(normalBoardButton, bottomRow);
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
        normalBoardButton.setOnAction(e -> {
            System.out.println("Normal board selected");
        });
        largeBoardButton.setOnAction(e -> {
            System.out.println("Large board selected");
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
        String css = getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        return scene;
    }
}