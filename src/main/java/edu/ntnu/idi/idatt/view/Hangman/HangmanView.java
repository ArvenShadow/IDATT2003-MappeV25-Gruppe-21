package edu.ntnu.idi.idatt.view.Hangman;

import edu.ntnu.idi.idatt.Controller.HangmanController;
import edu.ntnu.idi.idatt.model.GameState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Set;

public class HangmanView {

    private final Stage stage;
    private final HangmanController controller;

    private Label wordLabel;
    private Label guessesLabel;
    private Label statusLabel;
    private BorderPane mainLayout;
    private FlowPane letterButtons;
    private Button newGameButton;
    private Button hintButton;

    private static final String[] HANGMAN_IMAGES = {
            "/images/hangman_0.jpg",
            "/images/hangman_1.jpg",
            "/images/hangman_2.jpg",
            "/images/hangman_3.jpg",
            "/images/hangman_4.jpg",
            "/images/hangman_5.jpg",
            "/images/hangman_6.jpg"
    };

    public HangmanView(Stage stage) {
        this.stage = stage;
        this.controller = new HangmanController();
        setupStage();
        startNewGame();
    }

    private void setupStage() {

        mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        VBox topSection = createTopSection();
        mainLayout.setTop(topSection);

        VBox wordSection = createWordSection();
        mainLayout.setCenter(wordSection);

        VBox keyboardSection = createKeyboardSection();
        mainLayout.setBottom(keyboardSection);

        VBox controlSection = createControlSection();
        mainLayout.setRight(controlSection);

        updateHangmanImage(0);

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Hangman");
        stage.setScene(scene);

        stage.widthProperty().addListener((obs, oldVal, newVal) -> adjustSizing());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> adjustSizing());
        stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> adjustSizing());
    }

    private void adjustSizing() {

        double width = stage.getWidth();
        double height = stage.getHeight();

        double baseFontSize = Math.min(width, height) / 30;
        wordLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, baseFontSize * 1.5));
        guessesLabel.setFont(Font.font("Arial", baseFontSize));
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, baseFontSize));

        double buttonSize = Math.min(width, height) / 20;

        letterButtons.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setPrefSize(buttonSize, buttonSize);
                btn.setFont(Font.font("Arial", buttonSize / 2));
            }
        });

        newGameButton.setPrefWidth(Math.min(200, width / 4));
        hintButton.setPrefWidth(Math.min(200, width / 4));

        updateHangmanImage(controller.getWrongGuessCount());
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setAlignment(Pos.CENTER);

        statusLabel = new Label("Guess the word!");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10;");

        guessesLabel = new Label("Remaining gueses: " + controller.getRemainingGuesses());
        guessesLabel.setFont(Font.font("Arial", 16));
        guessesLabel.setTextFill(Color.WHITE);
        guessesLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 10;");

        topSection.getChildren().addAll(statusLabel, guessesLabel);
        return topSection;
    }

    private VBox createWordSection() {
        VBox wordSection = new VBox(20);
        wordSection.setAlignment(Pos.CENTER);

        wordLabel = new Label();
        wordLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 36));
        wordLabel.setTextFill(Color.WHITE);
        wordLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 15;");

        wordSection.getChildren().add(wordLabel);
        return wordSection;
    }

    private VBox createKeyboardSection() {
        VBox keyboardSection = new VBox(20);
        keyboardSection.setAlignment(Pos.CENTER);

        letterButtons = new FlowPane(10, 10);
        letterButtons.setAlignment(Pos.CENTER);

        for (char c = 'A'; c <= 'Z'; c++) {
            Button letterButton = createLetterButton(c);
            letterButtons.getChildren().add(letterButton);
        }

        keyboardSection.getChildren().add(letterButtons);
        return keyboardSection;
    }

    private Button createLetterButton(char letter) {
        Button button = new Button(String.valueOf(letter));
        button.setPrefSize(50, 50);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setOnAction(e -> makeGuess(letter));
        return button;
    }

    private VBox createControlSection() {
        VBox controlSection = new VBox(20);
        controlSection.setAlignment(Pos.CENTER);
        controlSection.setPadding(new Insets(15));
        controlSection.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        newGameButton = new Button("New game");
        newGameButton.setFont(Font.font("Arial", 14));
        newGameButton.setOnAction(e -> startNewGame());

        hintButton = new Button("Hint");
        hintButton.setFont(Font.font("Arial", 14));
        hintButton.setOnAction(e -> giveHint());

        controlSection.getChildren().addAll(newGameButton, hintButton);
        return controlSection;
    }

    private void startNewGame() {
        controller.startNewGame();
        updateUI();
        resetButtons();
    }

    private void makeGuess(char letter) {
        if (controller.isLetterAlreadyGuessed(letter)) {
            return;
        }

        boolean correct = controller.makeGuess(letter);
        updateUI();


        for (var node : letterButtons.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.getText().equalsIgnoreCase(String.valueOf(letter))) {
                    btn.setDisable(true);
                    if (correct) {
                        btn.setStyle("-fx-background-color: #90EE90;"); // Riktig (lysegrønn)
                    } else {
                        btn.setStyle("-fx-background-color: #FFB6C1;"); // Feil (lysrød)
                    }
                    break;
                }
            }
        }

        checkGameState();
    }

    private void giveHint() {
        Character hint = controller.getHint();
        if (hint != null) {
            updateUI();


            for (var node : letterButtons.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    if (btn.getText().equalsIgnoreCase(String.valueOf(hint))) {
                        btn.setDisable(true);
                        btn.setStyle("-fx-background-color: #FFD700;"); // Gul for hint
                        break;
                    }
                }
            }

            checkGameState();
        }
    }

    private void updateUI() {

        String wordState = controller.getCurrentWordState();
        StringBuilder displayWord = new StringBuilder();
        for (int i = 0; i < wordState.length(); i++) {
            displayWord.append(wordState.charAt(i)).append(" ");
        }
        wordLabel.setText(displayWord.toString().trim());


        guessesLabel.setText("Remaining guesses: " + controller.getRemainingGuesses());


        updateHangmanImage(controller.getWrongGuessCount());
    }

    private void updateHangmanImage(int wrongGuesses) {
        try {

            int index = Math.min(wrongGuesses, HANGMAN_IMAGES.length - 1);


            Image image = new Image(getClass().getResourceAsStream(HANGMAN_IMAGES[index]),
                    stage.getWidth(), stage.getHeight(), false, true);


            BackgroundImage backgroundImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );

            mainLayout.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Kunne ikke laste bakgrunnsbilde: " + e.getMessage());

            mainLayout.setBackground(new Background(new BackgroundFill(
                    Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private void checkGameState() {
        if (controller.isGameOver()) {
            GameState state = controller.getGameState();
            if (state == GameState.WON) {
                showGameOverDialog("Yay!", "You won! The word was: " + controller.revealWord());
            } else if (state == GameState.LOST) {
                showGameOverDialog("Sorry!", "You lost. The word was: " + controller.revealWord());
            }
        }
    }

    private void resetButtons() {
        for (var node : letterButtons.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setDisable(false);
                btn.setStyle("");
            }
        }
    }

    private void showGameOverDialog(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            Button newGameBtn = new Button("New game");
            newGameBtn.setOnAction(e -> {
                alert.close();
                startNewGame();
            });

            alert.getDialogPane().setExpandableContent(newGameBtn);
            alert.getDialogPane().setExpanded(true);

            alert.showAndWait();
        });
    }

    public void show() {
        stage.show();
    }
}
