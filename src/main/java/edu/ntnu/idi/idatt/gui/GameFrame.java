package edu.ntnu.idi.idatt.gui;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;


public class GameFrame extends Application{

  @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Label label = new Label("Hello World!");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}


