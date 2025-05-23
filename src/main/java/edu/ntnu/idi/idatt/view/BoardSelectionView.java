package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The BoardSelectionView class represents the view for selecting a board size in the application.
 * It provides a layout with buttons for choosing different board sizes or exiting the application.
 * This class is responsible for generating and styling the interface, as well as handling
 * user interactions such as button clicks.
 */
public class BoardSelectionView {

  public BoardSelectionView() {

  }

    /**
     * Constructs the main menu layout for the board selection view.
     *
     * @return a BorderPane representing the main menu layout with buttons for board selection and exit functionality.
     */
  public BorderPane getMainMenuLayout() {

    Button smallBoardButton = new Button("Small board (6x6)");
    Button standardBoardButton = new Button("Normal board (10x10)");
    Button largeBoardButton = new Button("Large board (12x12)");
    Button exitButton = new Button("Exit");

    smallBoardButton.getStyleClass().add("button");
    standardBoardButton.getStyleClass().add("button");
    largeBoardButton.getStyleClass().add("button");
    exitButton.getStyleClass().add("exit-button");

    HBox bottomRow = new HBox(20);
    bottomRow.getChildren().addAll(smallBoardButton, largeBoardButton);
    bottomRow.setAlignment(Pos.CENTER);

    VBox centerLayout = new VBox(15);
    centerLayout.getChildren().addAll(standardBoardButton, bottomRow);
    centerLayout.setAlignment(Pos.CENTER);

    centerLayout.setPadding(new Insets(20, 20, 50, 20));

    BorderPane layout = new BorderPane();
    layout.setCenter(centerLayout);
    layout.setBottom(exitButton);
    BorderPane.setAlignment(exitButton, Pos.BOTTOM_LEFT);
    BorderPane.setMargin(exitButton, new Insets(10));

    smallBoardButton.setOnAction(e -> {
      NavigationManager.getInstance().selectBoardAndContinue(
            "src/main/resources/boards/small_board.json");
    });
    standardBoardButton.setOnAction(e -> {
      NavigationManager.getInstance().selectBoardAndContinue(
            "src/main/resources/boards/standard_board.json");
    });
    largeBoardButton.setOnAction(e -> {
      NavigationManager.getInstance().selectBoardAndContinue(
            "src/main/resources/boards/large_board.json");
    });

    exitButton.setOnAction(e -> {
      System.out.println("Exiting the program...");
      System.exit(0);
    });

    return layout;
  }

    /**
     * Retrieves the root layout for the board selection view, applying necessary styles
     * and ensuring proper configuration for the main menu interface.
     *
     * @return a Parent object representing the root layout of the board selection view,
     *         styled with the designated CSS and containing the main menu components.
     */
  public Parent getRoot() {
    BorderPane layout = getMainMenuLayout();
    layout.getStyleClass().add("main-menu");


    try {
      String css = getClass().getResource("/styles/Select_board.css").toExternalForm();
      if (css != null) {
        layout.getStylesheets().add(css);
      }
    } catch (Exception e) {
      System.err.println("CSS not found: " + e.getMessage());
    }

    return layout;
  }
}