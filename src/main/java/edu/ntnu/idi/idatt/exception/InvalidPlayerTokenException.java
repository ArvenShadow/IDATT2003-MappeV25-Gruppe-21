package edu.ntnu.idi.idatt.exception;

/**
 * Represents an exception thrown when an invalid player token is encountered in a board game.
 */

public class InvalidPlayerTokenException extends BoardGameException {
  public InvalidPlayerTokenException(String message) {
    super(message);
  }
}