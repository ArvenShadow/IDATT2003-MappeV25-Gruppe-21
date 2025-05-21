package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.controller.BoardSelectionController;
import edu.ntnu.idi.idatt.view.BoardSelectionView;
import edu.ntnu.idi.idatt.view.MainMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {


    BoardSelectionController controller = new BoardSelectionController(primaryStage);
    BoardSelectionView selectionView = new BoardSelectionView(controller);


    MainMenuView mainMenuView = new MainMenuView();
    Scene mainMenuScene = new Scene(mainMenuView.getRoot());


    mainMenuScene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());


    mainMenuView.setNewGameHandler(() -> {
      Scene boardSelectionScene = selectionView.getStyledScene(); // Stylet board selection
      primaryStage.setScene(boardSelectionScene);
    });


    primaryStage.setScene(mainMenuScene);
    primaryStage.setTitle("Board Selection");
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}