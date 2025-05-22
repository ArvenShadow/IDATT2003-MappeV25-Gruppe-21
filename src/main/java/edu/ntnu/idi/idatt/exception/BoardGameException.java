package edu.ntnu.idi.idatt.exception;

/**
 * Represents a custom exception specific to board game-related errors.
 *
 * <p>This is a base exception class intended to be extended by more specific
 * exceptions that handle various issues which might arise in the context of
 * board game operations. Examples include invalid board configurations,
 * invalid moves, file operations, or invalid player tokens.
 */

public class BoardGameException extends Exception {
  public BoardGameException(String message) {
    super(message);
  }

  public BoardGameException(String message, Throwable cause) {
    super(message, cause);
  }
}
