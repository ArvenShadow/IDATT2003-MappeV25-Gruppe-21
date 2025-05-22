package edu.ntnu.idi.idatt.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

class DieTest {
  private Die die;

  @BeforeEach
  void setUp() {
    die = new Die();
  }

  @Test
  @DisplayName("Constructor initializes die with valid value")
  void testConstructorInitialization() {
    int value = die.getValue();
    assertTrue(value >= 1 && value <= 6,
      "Initial value must be between 1 and 6, was: " + value);
  }

  @Test
  @DisplayName("roll() returns value within valid range")
  void testRollReturnsValidRange() {
    for (int i = 0; i < 100; i++) {
      int rolled = die.roll();
      assertTrue(rolled >= 1 && rolled <= 6,
        "Roll must return 1-6, got: " + rolled);
    }
  }

  @Test
  @DisplayName("getValue() returns last rolled value")
  void testGetValueReturnsLastRoll() {
    int firstRoll = die.roll();
    assertEquals(firstRoll, die.getValue());

    int secondRoll = die.roll();
    assertEquals(secondRoll, die.getValue());
  }

  @Test
  @DisplayName("Multiple rolls produce different values")
  void testRandomnessOfRolls() {
    Set<Integer> values = new HashSet<>();

    for (int i = 0; i < 60; i++) {
      values.add(die.roll());
    }

    assertTrue(values.size() >= 3,
      "Die should produce varied results, only got: " + values);
  }

  @Test
  @DisplayName("getValue() is consistent between calls")
  void testGetValueConsistency() {
    die.roll();
    int firstGet = die.getValue();

    for (int i = 0; i < 5; i++) {
      assertEquals(firstGet, die.getValue(),
        "getValue() should be consistent until next roll");
    }
  }

  @Test
  @DisplayName("Distribution is roughly uniform")
  void testDistributionFairness() {
    int[] counts = new int[6];
    int totalRolls = 6000;

    for (int i = 0; i < totalRolls; i++) {
      counts[die.roll() - 1]++;
    }

    for (int i = 0; i < 6; i++) {
      int count = counts[i];
      assertTrue(count > 700 && count < 1300,
        String.format("Value %d: %d times (expected ~1000)", i + 1, count));
    }
  }
}