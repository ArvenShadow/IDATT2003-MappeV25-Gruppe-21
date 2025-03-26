package edu.ntnu.idi.idatt;

import java.util.List;
import java.util.Scanner;

public class BoardGameApp {
  private BoardGame boardGame;
  private Scanner scanner;

  public BoardGameApp() {
    this.boardGame = new BoardGame();
    this.scanner = new Scanner(System.in);
  }

  public void start() {
    System.out.println("Welcome to Snakes and Ladders!");

    // Initialize game
    initializeGame();

    // Game loop
    playGame();

    // Display winner and cleanup
    endGame();

    scanner.close();
  }

  private void initializeGame() {
    // Create board (using factory)
    boardGame.createBoard();

    // Create dice
    boardGame.createDice();

    //boardGame.viewPlayersFromFile();

    //boardGame.addPlayersFromFile();

    oldOrNewPlayers();

    // Add players
    //addPlayers();

    // Place all players on the first tile
    placePlayers();
  }

  private void oldOrNewPlayers() {
    try {
      PlayerCsvHandler csvHandler = new PlayerCsvHandler(boardGame);
      List<Player> csvPlayers = csvHandler.readFromFile("Test_users.csv");

      if (csvPlayers.isEmpty()) {

        addPlayers();
        return;
      }


      System.out.println("Velg spillere:");
      System.out.println("0. Legg til nye spillere");


      for (int i = 0; i < csvPlayers.size(); i++) {
        Player player = csvPlayers.get(i);
        System.out.println((i + 1) + ". " + player.getName() + " (" + player.getTokenType() + ")");
      }

      System.out.println("\nTast inn numrene til spillerne du vil spille med (f.eks. 1 3)");
      String input = scanner.nextLine().trim();

      if (input.equals("0")) {

        addPlayers();
        return;
      }

      boardGame.getPlayers().clear();

      String[] selectedNumbers = input.split("\\s+");

      for (String numStr : selectedNumbers) {
        try {
          int index = Integer.parseInt(numStr) - 1;

          if (index >= 0 && index < csvPlayers.size()) {
            Player selectedPlayer = csvPlayers.get(index);
            boardGame.addPlayer(selectedPlayer);
            System.out.println("Legger til " + selectedPlayer.getName());
          } else {
            System.out.println("Ugyldig valg: " + numStr);
          }
        } catch (NumberFormatException e) {
          System.out.println("Ugyldig input: " + numStr);
        }
      }

      if (boardGame.getPlayers().isEmpty()) {
        addPlayers();
      }

    } catch (BoardGameException e) {
      System.err.println("Feil ved håndtering av spillere: " + e.getMessage());
      addPlayers();
    }
  }






  private void addPlayers() {
    System.out.print("Enter number of new players (1-4): ");
    int numPlayers = getIntInput(1, 4);

    String[] tokenTypes = {"TopHat", "RaceCar", "Dog", "Ship"};

    for (int i = 0; i < numPlayers; i++) {
      System.out.print("Enter name for Player " + (i+1) + ": ");
      String name = scanner.nextLine();

      System.out.println("Choose token type:");
      for (int j = 0; j < tokenTypes.length; j++) {
        System.out.println((j+1) + ". " + tokenTypes[j]);
      }
      System.out.print("Enter choice (1-" + tokenTypes.length + "): ");

      int tokenChoice = getIntInput(1, tokenTypes.length);
      String tokenType = tokenTypes[tokenChoice-1];

      Player player = new Player(name, boardGame, tokenType);
      boardGame.addPlayer(player);

      System.out.println(name + " will use the " + tokenType + " token.");


    }
  }

  private void placePlayers() {
    Tile startTile = boardGame.getBoard().getTile(1);
    for (Player player : boardGame.getPlayers()) {
      player.placeOnTile(startTile);
    }
  }

  private void playGame() {
    int roundNumber = 1;

    while (!boardGame.isFinished()) {
      System.out.println("\n=== Round " + roundNumber + " ===");
      displayPlayerPositions();

      System.out.println("\nPress Enter to play round...");
      scanner.nextLine();

      boardGame.playOneRound();

      roundNumber++;
    }
  }

  private void displayPlayerPositions() {
    for (Player player : boardGame.getPlayers()) {
      Tile currentTile = player.getCurrentTile();
      int position = currentTile != null ? currentTile.getTileId() : 0;
      System.out.println(player.getName() + " (" + player.getTokenType() +
        ") is on tile " + position);
    }
  }

  private void endGame() {
    System.out.println("\n=== Game Over ===");
    System.out.println("The winner is: " + boardGame.getWinner().getName() +
      " (" + boardGame.getWinner().getTokenType() + ")");

    System.out.println("Ønsker du å lagre spillerene til filen? (y/n)");
    String input = scanner.nextLine();
    if (input.equalsIgnoreCase("y")){
      boardGame.savePlayersToFile();
    }
  }

  private int getIntInput(int min, int max) {
    int choice = min;
    boolean validInput = false;

    while (!validInput) {
      try {
        String input = scanner.nextLine();
        choice = Integer.parseInt(input);

        if (choice >= min && choice <= max) {
          validInput = true;
        } else {
          System.out.print("Please enter a number between " + min +
            " and " + max + ": ");
        }
      } catch (NumberFormatException e) {
        System.out.print("Invalid input. Please enter a number: ");
      }
    }

    return choice;
  }

  public static void main(String[] args) {
    BoardGameApp app = new BoardGameApp();
    app.start();
  }
}