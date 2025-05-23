package edu.ntnu.idi.idatt.view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Clippy-style notification component that appears in the bottom-right corner
 * with smooth animations and non-intrusive behavior.
 */
public class ClippyNotification extends VBox {
  private static final Duration FADE_IN_DURATION = Duration.millis(300);
  private static final Duration SLIDE_IN_DURATION = Duration.millis(400);
  private static final Duration DISPLAY_DURATION = Duration.millis(3500);
  private static final Duration FADE_OUT_DURATION = Duration.millis(500);

  private ImageView clippyImage;
  private VBox speechBubble;
  private Label messageLabel;
  private SequentialTransition animation;

  /**
   * Creates an instance of the ClippyNotification class.
   * The notification consists of a UI layout featuring an animated Clippy-like character,
   * a speech bubble, and a message label, designed for displaying brief notifications to the user.
   */

  public ClippyNotification() {
    createUI();
    setupAnimation();
    setVisible(false);
    setManaged(false);
  }

  /**
   * Initializes and constructs the user interface components for the ClippyNotification.
   * The method defines a layout consisting of an animated Clippy character, a speech bubble
   * for displaying messages, and a tail connecting the speech bubble to Clippy.
   */

  private void createUI() {
    // Load Clippy image
    clippyImage = new ImageView();
    try {
      Image clippy = new Image(getClass().getResourceAsStream("/images/clippy.png"));
      clippyImage.setImage(clippy);
      clippyImage.setFitWidth(70);
      clippyImage.setFitHeight(70);
      clippyImage.setPreserveRatio(true);
    } catch (Exception e) {
      // Fallback: create a simple representation if image not found
      clippyImage.setFitWidth(70);
      clippyImage.setFitHeight(70);
      System.out.println("Clippy image not found, using placeholder");
    }

    // Create speech bubble
    speechBubble = new VBox();
    speechBubble.setPadding(new Insets(12, 16, 12, 16));
    speechBubble.setStyle(
        "-fx-background-color: #ffffff; "
        + "-fx-border-color: #2c3e50; "
        + "-fx-border-width: 2px; "
        + "-fx-border-radius: 12px; "
        + "-fx-background-radius: 12px; "
        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 3, 3);"
    );
    speechBubble.setMaxWidth(280);
    speechBubble.setMinWidth(200);
    speechBubble.setAlignment(Pos.CENTER);

    // Message label
    messageLabel = new Label();
    messageLabel.setFont(Font.font("Arial", 13));
    messageLabel.setTextFill(Color.web("#2c3e50"));
    messageLabel.setWrapText(true);
    messageLabel.setTextAlignment(TextAlignment.CENTER);
    messageLabel.setAlignment(Pos.CENTER);

    speechBubble.getChildren().add(messageLabel);

    // Create speech bubble tail pointing to Clippy
    Polygon tail = new Polygon();
    tail.getPoints().addAll(new Double[]{
        0.0, 0.0,
        18.0, -12.0,
        18.0, 12.0
    });
    tail.setFill(Color.WHITE);
    tail.setStroke(Color.web("#2c3e50"));
    tail.setStrokeWidth(2);

    // Layout: Speech bubble on left, tail, then Clippy on right
    HBox content = new HBox(3);
    content.setAlignment(Pos.CENTER_RIGHT);
    content.getChildren().addAll(speechBubble, tail, clippyImage);

    getChildren().add(content);
    setAlignment(Pos.CENTER_RIGHT);

    // Set preferred size to prevent layout issues
    setPrefWidth(380);
    setMaxWidth(400);
  }

  private void setupAnimation() {
    // Slide in from right
    TranslateTransition slideIn = new TranslateTransition(SLIDE_IN_DURATION, this);
    slideIn.setFromX(100);
    slideIn.setToX(0);

    // Fade in
    FadeTransition fadeIn = new FadeTransition(FADE_IN_DURATION, this);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);

    // Display pause
    PauseTransition pause = new PauseTransition(DISPLAY_DURATION);

    // Fade out
    FadeTransition fadeOut = new FadeTransition(FADE_OUT_DURATION, this);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);
    fadeOut.setOnFinished(e -> hideNotification());

    // Combine slide and fade in
    FadeTransition combinedIn = new FadeTransition(SLIDE_IN_DURATION, this);
    combinedIn.setFromValue(0.0);
    combinedIn.setToValue(1.0);

    animation = new SequentialTransition(combinedIn, pause, fadeOut);
  }

  /**
   * Shows a notification with the given message.
   *
   * @param message The message to display
   */
  public void showNotification(String message) {
    messageLabel.setText(message);
    displayNotification();
  }

  /**
   * Shows a notification with title and message.
   *
   * @param title The title of the notification
   * @param message The message content
   */
  public void showNotification(String title, String message) {
    String fullMessage = title;
    if (message != null && !message.trim().isEmpty()) {
      fullMessage += "\n\n" + message;
    }
    showNotification(fullMessage);
  }

  private void displayNotification() {
    setVisible(true);
    setManaged(true);
    setOpacity(0.0);
    setTranslateX(100); // Start off-screen to the right

    if (animation.getStatus() == SequentialTransition.Status.RUNNING) {
      animation.stop();
    }

    // Reset transforms
    setTranslateX(100);
    setOpacity(0.0);

    // Create new slide-in animation
    TranslateTransition slideIn = new TranslateTransition(SLIDE_IN_DURATION, this);
    slideIn.setFromX(100);
    slideIn.setToX(0);

    FadeTransition fadeIn = new FadeTransition(SLIDE_IN_DURATION, this);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);

    PauseTransition pause = new PauseTransition(DISPLAY_DURATION);

    FadeTransition fadeOut = new FadeTransition(FADE_OUT_DURATION, this);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);
    fadeOut.setOnFinished(e -> hideNotification());

    // Play animations simultaneously
    slideIn.play();
    fadeIn.play();

    fadeIn.setOnFinished(e -> {
      pause.setOnFinished(ev -> fadeOut.play());
      pause.play();
    });
  }

  private void hideNotification() {
    setVisible(false);
    setManaged(false);
    // Reset transforms for next use
    setTranslateX(0);
    setOpacity(1.0);
  }
}