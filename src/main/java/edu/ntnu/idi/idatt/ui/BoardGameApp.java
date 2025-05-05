package edu.ntnu.idi.idatt.ui;

import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.view.MainMenu.MainMenu;
import edu.ntnu.idi.idatt.Controller.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class BoardGameApp extends Application {

  public static Stage stage;
  private BoardGame boardGame;
  private Scanner scanner;


  public BoardGameApp() {

  }

  @Override
  public void start(Stage primaryStage) {

    stage = primaryStage;


    MenuController menuController = new MenuController();


    MainMenu mainMenu = new MainMenu(primaryStage, menuController);
    mainMenu.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}