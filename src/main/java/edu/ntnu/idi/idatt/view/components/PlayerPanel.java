package edu.ntnu.idi.idatt.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

/**
 * A graphical panel representing a player in a game. The panel allows user interaction for
 * setting the player's name, selecting a token, and toggling the active state of the player.
 * It uses JavaFX components to provide a visual layout and manages internal state accordingly.
 */
public class PlayerPanel {
  private final int playerId;
  private boolean active;
  private GridPane pane;
  private TextField nameField;
  private ComboBox<String> tokenComboBox;
  private Button toggleButton;
  private ImageView tokenImageView;
  private List<String> availableTokens;

  /**
   * Constructs a new PlayerPanel with the given player details and available tokens.
   * This constructor initializes the player panel, sets up graphical components,
   * and manages the panel's state.
   *
   * @param playerId The unique identifier for the player.
   * @param active Indicates whether the player panel is initially active.
   * @param availableTokens A list of token names available for selection in the panel.
   */
  public PlayerPanel(int playerId, boolean active, List<String> availableTokens) {
    this.playerId = playerId;
    this.active = active;
    this.availableTokens = availableTokens;
    createPanel();
  }

  /**
   * Initializes and configures the primary layout panel for the PlayerPanel.
   * This method creates a GridPane to serve as the main container for the
   * player panel's graphical components.
   */
  private void createPanel() {
    pane = new GridPane();
    pane.setPadding(new Insets(10));
    pane.setHgap(10);
    pane.setVgap(10);
    pane.setPrefSize(250, 150);
    pane.setBorder(new Border(new BorderStroke(
      Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT
    )));

    createComponents();
    layoutComponents();
    setupEventHandlers();
    updatePanelState();
  }

  /**
   * Initializes and configures the components for the player panel.
   * This method sets up various graphical elements such as labels, input fields,
   * a combo box for token selection, and a button to toggle the panel state.
   * The method also sets default values and visual properties for each component.
   */
  private void createComponents() {
    Label titleLabel = new Label("Player " + playerId);
    titleLabel.setFont(javafx.scene.text.Font.font("Arial", 16));

    nameField = new TextField();
    nameField.setPromptText("Enter player name");
    nameField.setText("Player " + playerId);

    tokenComboBox = new ComboBox<>();
    tokenComboBox.getItems().addAll(availableTokens);
    tokenComboBox.setPromptText("Select token");

    tokenImageView = new ImageView();
    tokenImageView.setFitWidth(40);
    tokenImageView.setFitHeight(40);
    tokenImageView.setPreserveRatio(true);

    toggleButton = new Button(active ? "Remove" : "Add");

    // Store title label for layout
    pane.add(titleLabel, 0, 0, 2, 1);
  }

  /**
   * Arranges and positions the user interface components within the pane.
   * This method is responsible for adding graphical elements.
   */
  private void layoutComponents() {
    pane.add(new Label("Name:"), 0, 1);
    pane.add(nameField, 1, 1);
    pane.add(new Label("Token:"), 0, 2);
    pane.add(tokenComboBox, 1, 2);
    pane.add(tokenImageView, 2, 2);

    // Only show toggle button for players 3 and 4
    if (playerId > 2) {
      pane.add(toggleButton, 0, 3, 3, 1);
    }
  }

  /**
   * Configures the event handlers for user interactions within the player panel.
   * This method sets up listeners and actions for the graphical components, ensuring
   * dynamic and interactive behavior.
   */
  private void setupEventHandlers() {
    tokenComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      updateTokenPreview(newVal);
    });

    toggleButton.setOnAction(e -> {
      active = !active;
      updatePanelState();
    });
  }

  /**
   * Updates the token preview image displayed in the UI.
   * This method attempts to load an image corresponding to the specified token name
   * and updates the token image view. If the token name is null or an exception occurs
   * during image loading, the token image view is reset to null.
   *
   * @param tokenName The name of the token whose preview image should be displayed.
   *                  If null, the preview image will be cleared.
   */
  private void updateTokenPreview(String tokenName) {
    if (tokenName != null) {
      try {
        Image tokenImage = new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/images/tokens/" + tokenName.toLowerCase() + ".png")
        ));
        tokenImageView.setImage(tokenImage);
      } catch (Exception e) {
        tokenImageView.setImage(null);
      }
    } else {
      tokenImageView.setImage(null);
    }
  }

  /**
   * Updates the state of the player panel by adjusting the properties of its
   * graphical components based on the `active` state.
   */
  private void updatePanelState() {
    toggleButton.setText(active ? "Remove" : "Add");
    nameField.setDisable(!active);
    tokenComboBox.setDisable(!active);
    pane.setOpacity(active ? 1.0 : 0.6);
  }

  // Public getters and setters
  public GridPane getPane() {
    return pane;
  }

  public int getPlayerId() {
    return playerId;
  }

  public boolean isActive() {
    return active;
  }

  public String getPlayerName() {
    return nameField.getText();
  }

  public String getSelectedToken() {
    return tokenComboBox.getValue();
  }

  public void setActive(boolean active) {
    this.active = active;
    updatePanelState();
  }

  public void setPlayerName(String name) {
    nameField.setText(name);
  }

  public void setSelectedToken(String token) {
    tokenComboBox.setValue(token);
  }

  public void clearData() {
    nameField.setText("Player " + playerId);
    tokenComboBox.setValue(null);
  }

  /**
   * Determines whether the player panel contains valid data.
   * Valid data is defined as:
   * - The panel is active.
   * - The player's name is not null, not empty, and not composed solely of whitespace.
   * - A token is selected from the available options.
   *
   * @return true if the panel contains valid data, false otherwise.
   */
  public boolean hasValidData() {
    return active &&
      getPlayerName() != null &&
      !getPlayerName().trim().isEmpty() &&
      getSelectedToken() != null;
  }
}