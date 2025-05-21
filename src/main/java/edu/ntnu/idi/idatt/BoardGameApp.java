package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.view.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class BoardGameApp extends Application {
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Chutes and Ladders");


        MainMenu mainMenu = new MainMenu();
        primaryStage.setScene(mainMenu.getStyledScene());
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");


        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}