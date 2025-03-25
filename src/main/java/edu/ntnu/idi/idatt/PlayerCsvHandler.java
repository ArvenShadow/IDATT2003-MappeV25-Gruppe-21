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
        List<Player> players = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String token = parts[1].trim();
                    players.add(new Player(name, game, token));
                }
            }
            return players;

        } catch (IOException e) {
            throw new BoardGameException("Could not read file " + fileName + e.getMessage(), e);
        }


    }

    @Override
    public void writeToFile(List<Player> data, String filename) throws BoardGameException {

    }
}
