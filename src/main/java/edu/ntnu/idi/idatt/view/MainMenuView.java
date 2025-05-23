package edu.ntnu.idi.idatt.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Represents the main menu view of the application. This class is responsible for creating
 * and managing the user interface of the main menu, which includes options for starting a
 * new game and exiting the application.
 */
public class MainMenuView {
  private BorderPane root;
  private Runnable newGameHandler;
  //private Runnable loadGameHandler;
  private Runnable exitHandler;

  /**
   * Constructs a new instance of the MainMenuView class.
   * This constructor initializes the user interface of the main menu by calling
   * the createUI method, which sets up the layout and components for the menu,
   * including buttons for starting a new game, and exiting the application.
   */
  public MainMenuView() {
    createUI();
  }

  private void createUI() {

    root = new BorderPane();
    root.setPadding(new Insets(20));
    root.getStyleClass().add("main-menu");


    Label titleLabel = new Label("Board Game");
    titleLabel.setFont(Font.font("Arial", 36));
    titleLabel.getStyleClass().add("title-label");

    Button newGameButton = new Button("New Game");
    //Button loadGameButton = new Button("Load Game");
    Button exitButton = new Button("Exit");


    for (Button button : new Button[]{newGameButton, exitButton}) {
      button.setPrefWidth(200);
      button.setPrefHeight(40);
      button.getStyleClass().add("menu-button");
    }

    newGameButton.setOnAction(e -> {
      if (newGameHandler != null) {
        newGameHandler.run();
      }
    });
//    loadGameButton.setOnAction(e -> {
//      if (loadGameHandler != null) {
//        loadGameHandler.run();
//      }
//    });
    exitButton.setOnAction(e -> {
      if (exitHandler != null) {
        exitHandler.run();
      }
    });


    VBox menuBox = new VBox(20);
    menuBox.setAlignment(Pos.CENTER);
    menuBox.getChildren().addAll(titleLabel, newGameButton, exitButton);


    root.setCenter(menuBox);
  }

  public Parent getRoot() {
    return root;
  }

  public void setNewGameHandler(Runnable handler) {
    this.newGameHandler = handler;
  }

//  public void setLoadGameHandler(Runnable handler) {
//    this.loadGameHandler = handler;
//  }

  public void setExitHandler(Runnable handler) {
    this.exitHandler = handler;
  }

  public Scene getStyledScene() {

    Scene scene = new Scene(root, 800, 600);

    try {

      String css = getClass().getResource("/styles/game.css").toExternalForm();
      scene.getStylesheets().add(css);
    } catch (NullPointerException e) {
      System.err.println("Could not load CSS file: /styles/MainMenu.css");
      e.printStackTrace();
    }

    return scene;
  }
}