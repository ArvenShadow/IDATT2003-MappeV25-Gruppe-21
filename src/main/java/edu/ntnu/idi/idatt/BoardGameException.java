package edu.ntnu.idi.idatt;

public class BoardGameException extends Exception {
  public BoardGameException(String message) {
    super(message);
  }

  public BoardGameException(String message, Throwable cause) {
    super(message, cause);
  }
}
