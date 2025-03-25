package edu.ntnu.idi.idatt;

import java.util.Scanner;

public class BoardGameApp {
  public static void main(String[] args) {

    // Initialiser spillet
    BoardGame boardGame = new BoardGame();
    boardGame.createBoard();
    boardGame.createDice();

    // Legg til spillere
    Scanner scanner = new Scanner(System.in);
    System.out.println("Velkommen til Stigespillet!!");
    System.out.print("Antall spillere: ");
    int antallSpillere = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    for (int i = 1; i <= antallSpillere; i++) {
      System.out.print("Navn på spiller " + i + ": ");
      String playerName = scanner.nextLine();
      Player player = new Player(playerName, boardGame);
      boardGame.addPlayer(player);
      player.placeOnTile(boardGame.getBoard().getTile(1)); // Start posisjon
    }

    // Spillet starter
    boolean gameWon = false;
    while (!gameWon) {
      for (Player player : boardGame.getPlayers()) {
        System.out.println(player.getName() + "'s tur! Trykk Enter for å kaste terningen.");
        scanner.nextLine();

        int roll = boardGame.getDice().Roll(); // Kast terninger
        System.out.println(player.getName() + " kastet " + roll + "!");

        try {
          player.move(roll); // Flytt spilleren
          if (player.getCurrentTile().getTileId() >= 100) {
            System.out.println(player.getName() + " har vunnet spillet!");
            gameWon = true;
            break;
          }
        } catch (IllegalStateException e) {
          System.out.println(e.getMessage());
        }
      }
    }

    scanner.close();
  }
}