package edu.ntnu.idi.idatt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerCsvHandler implements FileHandler<List<Player>> {


   private BoardGame game;

   public PlayerCsvHandler(BoardGame game){
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

    }
}
