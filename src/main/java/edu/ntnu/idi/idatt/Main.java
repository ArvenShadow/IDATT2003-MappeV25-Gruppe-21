package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.navigation.NavTo;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.BoardSelectionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    // Fjern koden som navigerer til START_SCREEN
    /* NavigationManager navManager = NavigationManager.getInstance();
       navManager.initialize(primaryStage, "Snakes and Ladders", 1300, 900);
       navManager.navigateTo(NavTo.START_SCREEN);
    */

    // Sett opp BoardSelectionView som første visning
    BoardSelectionView boardSelectionView = new BoardSelectionView();
    Scene scene = boardSelectionView.getStyledScene();

    // Sett scenen og vis vinduet
    primaryStage.setScene(scene);
    primaryStage.setTitle("Board Selection");
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}