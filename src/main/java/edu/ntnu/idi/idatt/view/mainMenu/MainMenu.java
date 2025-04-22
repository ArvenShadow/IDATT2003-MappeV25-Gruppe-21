package edu.ntnu.idi.idatt.view.mainMenu;

import edu.ntnu.idi.idatt.controller.MenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainMenu {

    private final Stage stage;
    private final MenuController controller;
    private static final int BUTTON_SIZE = 400;
    private static final int IMAGE_SIZE = 350;

    public MainMenu(Stage stage, MenuController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {

        BorderPane bordePane = new BorderPane();
        bordePane.setStyle("-fx-background-image: url('/images/texture.jpg');");

        GridPane gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(50);
        gameGrid.setVgap(50);
        gameGrid.setPadding(new Insets(20));


        VBox hangmanBox = createGameBox("Hangman", "/images/hangman.png", e -> controller.startHangman());
        VBox chutesBox = createGameBox("Chutes and Ladders", "/images/chutesandladders.jpg", e -> controller.startChutesAndLadders());
        VBox pongBox = createGameBox("Pong", "/images/pong.png", e -> controller.startPong());
        VBox ticTacToeBox = createGameBox("Tic Tac Toe", "/images/tictactoe.png", e -> controller.startTicTacToe());


        gameGrid.add(hangmanBox, 0, 0);
        gameGrid.add(chutesBox, 1, 0);
        gameGrid.add(pongBox, 0, 1);
        gameGrid.add(ticTacToeBox, 1, 1);

        bordePane.setCenter(gameGrid);


        Button exitButton = new Button("Avslutt");
        exitButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        exitButton.setPrefWidth(150);
        exitButton.setStyle("-fx-background-color: #FF5252; -fx-text-fill: white; -fx-background-radius: 10;");
        exitButton.setOnAction(e -> controller.exit());

        VBox bottomBox = new VBox(20, exitButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        bordePane.setBottom(bottomBox);

        Scene scene = new Scene(bordePane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Spillmeny");
        stage.setFullScreenExitHint(" ");
        stage.setFullScreen(true);
        stage.show();
    }

    private VBox createGameBox(String gameName, String imagePath, javafx.event.EventHandler<javafx.event.ActionEvent> action) {

        Label titleLabel = new Label(gameName);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        // Knapp med bilde
        Button gameButton = new Button();
        gameButton.setPrefWidth(BUTTON_SIZE);
        gameButton.setPrefHeight(BUTTON_SIZE);
        gameButton.setStyle("-fx-background-color: #4C9CF0; -fx-background-radius: 10;");


        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setFitHeight(IMAGE_SIZE);
            imageView.setPreserveRatio(true);
            gameButton.setGraphic(imageView);
            gameButton.setContentDisplay(ContentDisplay.CENTER);
        } catch (Exception e) {
            System.err.println("Kunne ikke laste bilde for " + gameName + ": " + e.getMessage());

            gameButton.setText(gameName);
        }


        gameButton.setOnMouseEntered(e -> gameButton.setStyle("-fx-background-color: #3A78B5; -fx-background-radius: 10;"));
        gameButton.setOnMouseExited(e -> gameButton.setStyle("-fx-background-color: #4C9CF0; -fx-background-radius: 10;"));
        gameButton.setOnAction(action);

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(titleLabel, gameButton);

        return box;
    }
}