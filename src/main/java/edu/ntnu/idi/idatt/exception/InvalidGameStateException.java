package edu.ntnu.idi.idatt.exception;

/**
 * Represents an exception specific to invalid game states in a board game.
 */

public class InvalidGameStateException extends RuntimeException {
  public InvalidGameStateException(String message) {
    super(message);
  }
}