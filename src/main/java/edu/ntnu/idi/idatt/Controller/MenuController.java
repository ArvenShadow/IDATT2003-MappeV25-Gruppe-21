package edu.ntnu.idi.idatt.Controller;

import edu.ntnu.idi.idatt.view.Hangman.HangmanView;
import javafx.stage.Stage;

public class MenuController {

    public void startChutesAndLadders(){
        System.out.println("Starting chutes and ladders...");
    }

    public void startTicTacToe(){
        System.out.println("Starting tic tac toe...");
    }

    public void startPong(){
        System.out.println("Starting pong...");
    }

    public void startConncectFour(){
        System.out.println("Starting connect four...");
    }

    public void startHangman(){
        System.out.println("Starting hangman...");
        Stage hangmanStage = new Stage();
        HangmanView hangmanView = new HangmanView(hangmanStage);
        hangmanView.show();
    }

    public void exit(){
        System.out.println("Exiting...");
        System.exit(0);
    }
}
