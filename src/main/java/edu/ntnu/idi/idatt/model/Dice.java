package edu.ntnu.idi.idatt.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Dice class represents a collection of dice, providing functionality
 * to roll all dice, retrieve previous roll results, and configure the number of dice.
 */
public class Dice {

  private ArrayList<Die> dice;
  private int[] lastRoll;

  /**
   * Initializes the Dice object with the specified number of dice.
   *
   * @param numberOfDice the number of dice to include in this Dice object.
   *                     Must be at least 1.
   */

  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("You must have at least 1 die");
    }
    dice = new ArrayList<>();
    lastRoll = new int[numberOfDice];
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die());
    }
  }

  /**
   * Rolls all dice in the collection and updates their last rolled values.
   *
   * @return an array containing the results of rolling each die in the collection.
   */

  public int[] rollAllDice() {
    int total = 0;
    lastRoll = new int[dice.size()];

    for (int i = 0; i < dice.size(); i++) {
      lastRoll[i] = dice.get(i).roll();
      total += lastRoll[i];
    }
    return lastRoll;
  }

  /**
   * Rolls all dice in the collection and calculates their total value.
   *
   * @return the total sum of the dice rolls after rolling all dice in the collection.
   */

  public int roll() {
    rollAllDice();
    return getTotal();
  }

  /**
   * Calculates and returns the total sum of the values from the last roll
   * of all dice in the collection.
   *
   * @return the total sum of the values rolled in the last roll.
   */
  public int getTotal() {
    int total = 0;
    for (int val : lastRoll) {
      total += val;
    }
    return total;
  }

  /**
   * Retrieves the value of the die at the specified position.
   *
   * @param dieNumber the index of the die to retrieve the value from.
   *                  Must be within the range of existing dice indices.
   * @return the value of the die at the specified index.
   */
  public int getDie(int dieNumber) {
    return dice.get(dieNumber).getValue();
  }

  public int[] getLastRoll() {
    return Arrays.copyOf(lastRoll, lastRoll.length);
  }

  public int getNumberOfDice() {
    return dice.size();
  }

  /**
   * Sets the number of dice in the collection. The number of dice can be increased or decreased.
   * If increasing the count, new dice are added. If decreasing the count, existing dice are removed.
   * The last roll array is updated to match the new number of dice.
   *
   * @param count the new number of dice for the collection. Must be at least 1.
   * @throws IllegalArgumentException if the count is less than 1.
   */
  public void setNumberOfDice(int count) {
    if(count < 1) {
      throw new IllegalArgumentException("Must have at least 1 die");
    }

    // If increasing dice count
    while (dice.size() < count) {
      dice.add(new Die());
    }

        // If decreasing dice count
        while (dice.size() > count) {
            dice.remove(dice.size() - 1);
        }

        lastRoll = new int[count];
    }
}