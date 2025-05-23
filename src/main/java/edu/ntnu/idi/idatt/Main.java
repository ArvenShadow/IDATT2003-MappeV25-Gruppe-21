package edu.ntnu.idi.idatt;

import edu.ntnu.idi.idatt.navigation.NavTo;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class serves as the entry point for the JavaFX application.
 * It initializes the primary stage and sets up the main navigation flow
 * for the application using the NavigationManager singleton. The application
 * begins by navigating to the START_SCREEN.
 *
 * <p>This class extends the {@code Application} class, which is the base class
 * for JavaFX application development. The {@code start} method is overridden
 * to configure the application's primary stage.
 */
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