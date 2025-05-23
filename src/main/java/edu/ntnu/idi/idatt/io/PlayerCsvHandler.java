package edu.ntnu.idi.idatt.io;

import edu.ntnu.idi.idatt.exception.BoardGameException;
import edu.ntnu.idi.idatt.exception.InvalidPlayerTokenException;
import edu.ntnu.idi.idatt.model.BoardGame;
import edu.ntnu.idi.idatt.model.Player;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles reading and writing operations for player data in CSV format.
 *
 * <p>This class facilitates managing player-related data for a board game application.
 * It implements the {@code FileHandler<List<Player>>} interface, providing functionality
 * to read player details from a CSV file and write player details back to a CSV file.
 *</p>
 *
 * <p>The CSV file format is expected to adhere to the following structure:
 * - The first line is a header: "PlayerName,PlayerToken".
 * - Subsequent lines contain player data in the format: "PlayerName,PlayerToken".
 *</p>
 *
 * <p>Instances of this class are tied to a specific {@code BoardGame}, which ensures
 * that the players belong to a valid game context.
 */
public class PlayerCsvHandler implements FileHandler<List<Player>> {

  private BoardGame game;
  private static final Set<String> ALLOWED_TOKENS = new HashSet<>();

  static {
    ALLOWED_TOKENS.add("TopHat");
    ALLOWED_TOKENS.add("RaceCar");
    ALLOWED_TOKENS.add("Shoe");
    ALLOWED_TOKENS.add("Thimble");
    ALLOWED_TOKENS.add("Cat");
  }

  /**
   * Constructs a new PlayerCsvHandler with the specified BoardGame instance.
   *
   * @param game the BoardGame instance to be associated with this handler.
   *             It must not be null. If null, an IllegalArgumentException is thrown.
   */
  public PlayerCsvHandler(BoardGame game) {
    if (game == null) {
      throw new IllegalArgumentException("BoardGame cannot be null");
    }
    this.game = game;
  }

  /**
   * Reads player data from a CSV file and constructs a list of Player objects.
   * The expected CSV format is "Name,Token", where each line corresponds to a player.
   * Validates token information against allowed tokens and ensures no duplicate tokens are used.
   *
   * @param fileName the name of the CSV file to read from
   * @return a list of Player objects constructed from the data in the file
   * @throws BoardGameException if the file can't be read, contains invalid data,
   *                            or no valid players are found
   */

  @Override
  public List<Player> readFromFile(String fileName) throws BoardGameException {
    List<Player> players = new ArrayList<>();
    Set<String> usedTokens = new HashSet<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      boolean isHeader = true;

      while ((line = reader.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }

        String[] parts = line.split(",");
        if (parts.length != 2) {
          throw new BoardGameException("Invalid CSV format. Expected: Name,Token");
        }

        String name = parts[0].trim();
        String token = parts[1].trim();

        // Validate token
        if (!ALLOWED_TOKENS.contains(token)) {
          throw new InvalidPlayerTokenException("Invalid token: " + token
            + ". Allowed: " + String.join(", ", ALLOWED_TOKENS));
        }

        if (usedTokens.contains(token)) {
          throw new BoardGameException("Duplicate token: " + token);
        }

        players.add(new Player(name, game, token));
        usedTokens.add(token);
      }

      if (players.isEmpty()) {
        throw new BoardGameException("No players found in file");
      }

      return players;

    } catch (IOException e) {
      throw new BoardGameException("Cannot read player file: " + fileName, e);
    }
  }

  /**
   * Writes a list of players to a CSV file. The format of the file is as follows:
   * the first line contains column headers ("PlayerName,PlayerToken"), and each
   * subsequent line contains the player's name and token, separated by a comma.
   *
   * <p>Each player's token is validated against a predefined list of allowed tokens.
   * If an invalid token is encountered, an exception is thrown, and the file is not written.
   *</p>
   *
   * @param players the list of Player objects to be written to the file. Each player
   *                must have a valid token type and a name.
   * @param filename the name of the CSV file to write the data to. It must be a valid
   *                 writable path on the filesystem.
   * @throws BoardGameException if an I/O error occurs during file writing or if the
   *                            player data contains invalid tokens.
   */

  @Override
  public void writeToFile(List<Player> players, String filename) throws BoardGameException {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write("PlayerName,PlayerToken\n");

      for (Player player : players) {
        String token = player.getTokenType();

        if (!ALLOWED_TOKENS.contains(token)) {
          throw new InvalidPlayerTokenException("Invalid token: " + token);
        }

        writer.write(player.getName() + "," + token + "\n");
      }

    } catch (IOException e) {
      throw new BoardGameException("Cannot write player file: " + filename, e);
    }
  }
}