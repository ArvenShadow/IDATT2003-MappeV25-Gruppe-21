package edu.ntnu.idi.idatt.model;

import java.util.Random;

/**
 * The Die class represents a single six-sided die that can be rolled to
 * generate random values between 1 and 6. It maintains state, allowing
 * retrieval of the last rolled value.
 */
public class Die {

  private Random random;
  private int lastRolledValue;

  /**
   * Constructs a new Die object. Initializes the die with a random number generator and rolls the die once,
   * setting its initial state to a randomly selected value between 1 and 6.
   */
  public Die() {
    random = new Random();
    roll();
  }

  public int roll() {
    lastRolledValue = random.nextInt(6) + 1;
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }
}
