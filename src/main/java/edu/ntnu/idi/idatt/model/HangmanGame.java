package edu.ntnu.idi.idatt.model;

import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class HangmanGame {

    public static final String[] wordList = {"apple", "banana", "carrot", "duck", "elephant", "fox", "giraffe", "horse", "jellyfish", "kiwi",
            "lion", "monkey", "octopus", "panda", "penguin", "rabbit", "shark", "snake", "spider", "tiger", "turtle", "whale", "zebra",
            "computer", "competition", "soulmate", "java", "python"};

    private String wordToGuess;
    private char[] currentGuess;
    private int wrongGuesses;
    private Set<Character> guessedLetters;
    private GameState state;

    private static final int MAX_WRONG_GUESSES = 6;

    public HangmanGame() {
        startNewGame();
    }

    public void startNewGame() {
        Random random = new Random();

        wordToGuess = wordList[random.nextInt(wordList.length)].toLowerCase();
        currentGuess = new char[wordToGuess.length()];

        for (int i = 0; i < currentGuess.length; i++) {
            currentGuess[i] = '_';
        }

        wrongGuesses = 0;
        guessedLetters = new HashSet<>();
        state = GameState.PLAYING;

    }

    public boolean guessLetter(char letter) {
        letter = Character.toLowerCase(letter);

        if (state != GameState.PLAYING || guessedLetters.contains(letter)) {
            return false;
        }

        guessedLetters.add(letter);

        boolean foundLetter = false;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == letter) {
                currentGuess[i] = letter;
                foundLetter = true;
            }
        }

        if (!foundLetter) {
            wrongGuesses++;
            if (wrongGuesses >= MAX_WRONG_GUESSES) {
                state = GameState.LOST;
            }
        } else if (String.valueOf(currentGuess).equals(wordToGuess)) {
            state = GameState.WON;
        }

        return foundLetter;

    }

    public String getCurrentGuessAsString() {
        return String.valueOf(currentGuess);
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public Set<Character> getGuessedLetters() {
        return new HashSet<>(guessedLetters);
    }

    public GameState getState() {
        return state;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public int getRemainingGuesses() {
        return MAX_WRONG_GUESSES - wrongGuesses;
    }

}


