package edu.ntnu.idi.idatt.factory;

import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.io.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.Board;

/**
 * A factory class responsible for creating instances of Board.
 * This class provides a method to create a Board instance by reading
 * its configuration from a predefined JSON file. If the JSON file
 * cannot be read or processed, a RuntimeException is thrown.
 */

public class BoardGameFactory {

  /**
   * Creates and returns a new instance of a Board by reading its configuration
   * from a predefined JSON file.
   *
   * This method utilizes the BoardJsonHandler to parse the JSON file and
   * construct a Board object. If the file cannot be read or the JSON configuration
   * is invalid, a RuntimeException is thrown indicating the error.
   *
   * @return a new Board instance configured based on the contents of the JSON file
   * @throws RuntimeException if the board configuration file cannot be read or processed
   */

  public static Board createBoard() {
    BoardJsonHandler handler = new BoardJsonHandler();
    try{
      return handler.readFromFile("src/main/resources/boards/standard_board.json");
    } catch (BoardGameException e){
      throw new RuntimeException("Could not create board from JSon file: " + e.getMessage());
    }
  }
}
