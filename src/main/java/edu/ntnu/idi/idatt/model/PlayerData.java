package edu.ntnu.idi.idatt.model;

/**
 * The PlayerData class is a class to temporarily create PlayerData for the
 * CharacterSelectionView class to later use the data to
 * instantiate a Player.
  */
public class PlayerData {
  private String name;
  private String token;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}