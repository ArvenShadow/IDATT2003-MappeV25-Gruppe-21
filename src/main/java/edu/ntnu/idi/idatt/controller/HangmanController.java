package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.HangmanGame;
import edu.ntnu.idi.idatt.model.GameState;
import java.util.Set;


public class HangmanController {

    private HangmanGame game;

    public HangmanController() {
        game = new HangmanGame();
    }

    public void startNewGame(){
        game.startNewGame();
    }

    public boolean makeGuess(char letter){
        return game.guessLetter(letter);
    }

    public String getCurrentWordState(){
        return game.getCurrentGuessAsString();
    }

    public GameState getGameState(){
        return game.getState();
    }

    public boolean isGameOver(){
        GameState state = game.getState();
        return state == GameState.WON || state == GameState.LOST;
    }

    public int getWrongGuessCount(){
        return game.getWrongGuesses();
    }

    public int getRemainingGuesses(){
        return game.getRemainingGuesses();
    }

    public Set<Character> getGuessedLetters(){
        return game.getGuessedLetters();
    }

    public String revealWord(){
        return game.getWordToGuess();
    }

    public boolean isLetterAlreadyGuessed(char letter){
        return game.getGuessedLetters().contains(Character.toLowerCase(letter));
    }

    public double getCompletionPercentage() {
        String currentGuess = game.getCurrentGuessAsString();
        String wordToGuess = game.getWordToGuess();

        int correctLetters = 0;
        for (int i = 0; i < currentGuess.length(); i++) {
            if (currentGuess.charAt(i) != '_') {
                correctLetters++;
            }
        }

        return (double) correctLetters / wordToGuess.length() * 100;
    }


    public Character getHint() {
        String word = game.getWordToGuess();
        String currentGuess = game.getCurrentGuessAsString();


        for (int i = 0; i < word.length(); i++) {
            if (currentGuess.charAt(i) == '_') {
                char hint = word.charAt(i);

                game.guessLetter(hint);
                return hint;
            }
        }

        return null;
    }

}
