package edu.ntnu.idi.idatt.exception;

/**
 * Represents an exception thrown when there is an invalid board configuration in a board game.
 *
 * This exception is a specific subclass of BoardGameException and is used to
 * signal issues specifically related to improper or invalid configurations of
 * the board within the context of a board game. Examples of such invalid
 * configurations could include incompatible board dimensions, missing
 * elements, or rules being violated in the setup process.
 */
public class InvalidBoardConfigurationException extends BoardGameException {
  public InvalidBoardConfigurationException(String message) {
    super(message);
  }

  public InvalidBoardConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}