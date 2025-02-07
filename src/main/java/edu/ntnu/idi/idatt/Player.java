package edu.ntnu.idi.idatt;

public class Player {
  String name;
  int position;

  void move(int steps) {
    position += steps;
  }
}
