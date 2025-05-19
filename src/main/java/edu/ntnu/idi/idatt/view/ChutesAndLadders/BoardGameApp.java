package edu.ntnu.idi.idatt.view.ChutesAndLadders;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BoardGameApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chutes and Ladders");

        VBox mainMenu = new VBox();
        mainMenu.setSpacing(10);
        mainMenu.getStyleClass().add("main-menu");



        Button loadBoardButton = new Button("Load Existing Board");
        Button createBoardButton = new Button("Create New Board");
        Button startGameButton = new Button("Start Game");
        Button exitButton = new Button("Exit");

        mainMenu.getChildren().addAll(loadBoardButton, createBoardButton, startGameButton);

        Scene mainScene = new Scene(mainMenu, 600, 400);
        mainScene.getStylesheets().add("style.css");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
