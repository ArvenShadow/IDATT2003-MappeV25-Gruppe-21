package edu.ntnu.idi.idatt.view.ingame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

/**
 * Settings panel that can be integrated into the game view.
 * Provides controls for game settings without using dialog popups.
 */
public class SettingsPanel extends VBox {
  private Spinner<Integer> diceCountSpinner;
  private Button applyButton;
  private Button closeButton;
  private Consumer<Integer> onDiceCountChanged;
  private Runnable onClose;

  public SettingsPanel(int currentDiceCount) {
    createUI(currentDiceCount);
    stylePanel();
  }

  /**
   * Creates and initializes the user interface elements for the settings panel.
   * This includes a title, a spinner for selecting the number of dice,
   * and buttons for applying or closing the settings.
   *
   * @param currentDiceCount the initial number of dice to be displayed in the spinner
   */
  private void createUI(int currentDiceCount) {
    // Title
    Label titleLabel = new Label("Settings");
    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

    // Dice count setting
    Label diceLabel = new Label("Dice:");
    diceCountSpinner = new Spinner<>(1, 5, currentDiceCount);
    diceCountSpinner.setEditable(true);
    diceCountSpinner.setPrefWidth(60);

    HBox diceRow = new HBox(10, diceLabel, diceCountSpinner);
    diceRow.setAlignment(Pos.CENTER_LEFT);

    // Buttons
    applyButton = new Button("Apply");
    closeButton = new Button("Close");

    // Make buttons smaller
    applyButton.setPrefWidth(100);
    closeButton.setPrefWidth(100);

    applyButton.setOnAction(e -> handleApply());
    closeButton.setOnAction(e -> handleClose());

    HBox buttonRow = new HBox(8, applyButton, closeButton);
    buttonRow.setAlignment(Pos.CENTER);

    // Layout
    getChildren().addAll(titleLabel, diceRow, buttonRow);

    setSpacing(12);
  }

  /**
   * Configures the styling and layout for the panel.
   * This includes setting padding, background and border styling,
   * size constraints, and styles for specific child components such as buttons
   * and a spinner.
   */
  private void stylePanel() {
    setPadding(new Insets(12));
    setStyle(
      "-fx-background-color: #f8f8f8; " +
        "-fx-border-color: #cccccc; " +
        "-fx-border-width: 1px; " +
        "-fx-border-radius: 8px; " +
        "-fx-background-radius: 8px;"
    );

    // Set size constraints to keep it compact
    setPrefWidth(250);
    setMaxWidth(300);
    setPrefHeight(120);
    setMaxHeight(160);

    // Style buttons
    applyButton.getStyleClass().add("button");
    closeButton.getStyleClass().add("button");

    // Make sure the spinner has good styling
    diceCountSpinner.getStyleClass().add("spinner");
  }

  /**
   * Handles the application of changes to the number of dice in the settings panel.
   */
  private void handleApply() {
    int newDiceCount = diceCountSpinner.getValue();
    if (onDiceCountChanged != null) {
      onDiceCountChanged.accept(newDiceCount);
      onClose.run();
    }
  }

  /**
   * Handles the closing action of the settings panel.
   */
  private void handleClose() {
    if (onClose != null) {
      onClose.run();
    }
  }

  /**
   * Sets a handler that will be triggered whenever the dice count is updated.
   *
   * @param handler a Consumer function that accepts an integer representing
   *                the updated dice count.
   */
  public void setOnDiceCountChanged(Consumer<Integer> handler) {
    this.onDiceCountChanged = handler;
  }

  /**
   * Sets a handler that will be called when the close action is triggered.
   *
   * @param handler a Runnable object that defines the behavior to execute upon the close event
   */
  public void setOnClose(Runnable handler) {
    this.onClose = handler;
  }

  public int getDiceCount() {
    return diceCountSpinner.getValue();
  }

  /**
   * Updates the dice count displayed in the spinner to the specified value.
   * This method modifies the value shown in the diceCountSpinner's ValueFactory.
   *
   * @param count the new number of dice to be displayed in the spinner
   */
  public void updateDiceCount(int count) {
    diceCountSpinner.getValueFactory().setValue(count);
  }
}