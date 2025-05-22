package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.navigation.NavTo;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.BoardSelectionView;
import edu.ntnu.idi.idatt.view.MainMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  @Override

  public void start(Stage primaryStage) {

    NavigationManager nav = NavigationManager.getInstance();
    nav.initialize(primaryStage, "Board Game", 800, 600);
    nav.navigateTo(NavTo.START_SCREEN);

    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}