package edu.ntnu.idi.idatt;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerCsvHandler implements FileHandler<List<Player>> {


    private BoardGame game;

    public PlayerCsvHandler(BoardGame game) {
        this.game = game;
    }

    @Override
    public List<Player> readFromFile(String fileName) throws BoardGameException {
        String file = "src\\Test_users.csv";
        List<Player> players = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = reader.readLine();


            while ((line = reader.readLine()) != null) {

                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");


                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        String token = parts[1].trim();


                        String[] validTokens = {"TopHat", "RaceCar", "Dog", "Ship"};
                        boolean isValid = false;

                        for (String validToken : validTokens) {

                            if (validToken.equalsIgnoreCase(token)) {
                                token = validToken; // Normalisér token
                                isValid = true;
                                break;
                            }
                        }

                        if (isValid) {
                            Player player = new Player(name, game, token);
                            players.add(player);
                        } else {
                            System.err.println("Ugyldig token for spiller: " + name + " - " + token);
                        }
                    } else {
                        System.err.println("Ugyldig CSV-format: " + line);
                    }
                }
            }

            return players;
        } catch (IOException e) {
            throw new BoardGameException("Kunne ikke lese fil " + fileName, e);
        }
    }


    @Override
    public void writeToFile(List<Player> data, String filename) throws BoardGameException {
        String file = "src\\Test_users.csv";
        try {

            List<Player> existingPlayers = readFromFile(filename);


            Set<Player> uniquePlayers = new HashSet<>(existingPlayers);

            // Legg til nye spillere
            for (Player newPlayer : data) {
                // Sjekk om spilleren allerede eksisterer basert på navn og token
                boolean playerExists = existingPlayers.stream()
                        .anyMatch(p -> p.getName().equals(newPlayer.getName()) &&
                                p.getTokenType().equals(newPlayer.getTokenType()));

                if (!playerExists) {
                    uniquePlayers.add(newPlayer);
                }
            }

            // Skriv alle unike spillere til filen
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Skriv header
                writer.write("Player,PlayerToken");
                writer.newLine();

                // Skriv spillere
                for (Player player : uniquePlayers) {
                    String line = player.getName() + "," + player.getTokenType();
                    writer.write(line);
                    writer.newLine();
                }

                System.out.println("Spillere lagret i " + file);
            }
        } catch (BoardGameException | IOException e) {
            throw new BoardGameException("Kunne ikke skrive til fil " + filename, e);
        }
    }
}



