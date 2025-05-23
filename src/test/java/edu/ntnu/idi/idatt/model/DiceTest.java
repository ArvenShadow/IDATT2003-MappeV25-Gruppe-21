package edu.ntnu.idi.idatt.model;

import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
  private Dice dice;

  @BeforeEach
  void setUp() {
    dice = new Dice(2);
  }

  @Test
  @DisplayName("Constructor with valid number of dice")
  void testValidConstructor() {
    assertDoesNotThrow(() -> new Dice(1));
    assertDoesNotThrow(() -> new Dice(5));
    assertEquals(2, dice.getNumberOfDice());
  }

  @Test
  @DisplayName("Constructor should throw exception for invalid dice count")
  void testInvalidConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new Dice(0));
    assertThrows(IllegalArgumentException.class, () -> new Dice(-1));
  }

  @Test
  @DisplayName("rollAllDice() returns array with correct length")
  void testRollAllDiceArrayLength() {
    int[] results = dice.rollAllDice();
    assertEquals(2, results.length);

    Dice threeDice = new Dice(3);
    results = threeDice.rollAllDice();
    assertEquals(3, results.length);
  }

  @Test
  @DisplayName("rollAllDice() returns valid values")
  void testRollAllDiceValidValues() {
    for (int i = 0; i < 50; i++) {
      int[] results = dice.rollAllDice();
      for (int value : results) {
        assertTrue(value >= 1 && value <= 6,
          "Each die should roll between 1-6, but got " + value);
      }
    }
  }

  @Test
  @DisplayName("Roll() returns sum of all dice")
  void testRollReturnsTotal() {
    int total = dice.roll();
    assertTrue(total >= 2 && total <= 12,
      "Two dice should sum between 2-12, but got " + total);

    // Verify Roll() equals getTotal()
    assertEquals(dice.getTotal(), total);
  }

  @Test
  @DisplayName("getTotal() returns correct sum")
  void testGetTotal() {
    dice.rollAllDice();
    int total = dice.getTotal();

    int expectedTotal = 0;
    for (int value : dice.getLastRoll()) {
      expectedTotal += value;
    }

    assertEquals(expectedTotal, total);
  }

  @Test
  @DisplayName("getDie() returns individual die value")
  void testGetDie() {
    dice.roll();

    // Test valid indices
    assertTrue(dice.getDie(0) >= 1 && dice.getDie(0) <= 6);
    assertTrue(dice.getDie(1) >= 1 && dice.getDie(1) <= 6);

    // Test out of bounds
    assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(2));
  }

  @Test
  @DisplayName("getLastRoll() returns copy of array")
  void testGetLastRollReturnsCopy() {
    dice.roll();
    int[] firstCall = dice.getLastRoll();
    int[] secondCall = dice.getLastRoll();

    // Should be equal values but different arrays
    assertArrayEquals(firstCall, secondCall);
    assertNotSame(firstCall, secondCall, "Should return a copy, not the original array");
  }

  @Test
  @DisplayName("setNumberOfDice() changes dice count correctly")
  void testSetNumberOfDice() {
    dice.setNumberOfDice(3);
    assertEquals(3, dice.getNumberOfDice());

    int[] results = dice.rollAllDice();
    assertEquals(3, results.length);

    // Test decreasing
    dice.setNumberOfDice(1);
    assertEquals(1, dice.getNumberOfDice());
    results = dice.rollAllDice();
    assertEquals(1, results.length);
  }

  @Test
  @DisplayName("setNumberOfDice() throws exception for invalid count")
  void testSetNumberOfDiceInvalid() {
    assertThrows(IllegalArgumentException.class, () -> dice.setNumberOfDice(0));
    assertThrows(IllegalArgumentException.class, () -> dice.setNumberOfDice(-1));
  }

  @Test
  @DisplayName("Multiple dice produce varied totals")
  void testMultipleDiceDistribution() {
    Set<Integer> totals = new HashSet<>();

    for (int i = 0; i < 100; i++) {
      totals.add(dice.roll());
    }

    // With 2 dice, should get multiple different totals
    assertTrue(totals.size() > 5,
      "Two dice should produce varied totals, but only got: " + totals.size());
  }
}