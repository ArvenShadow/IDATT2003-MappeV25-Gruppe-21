package edu.ntnu.idi.idatt;

public class LadderAction implements TileAction{

  private int endPosition;

  public LadderAction(int endPosition){
    this.endPosition = endPosition;
  }

  @Override
  public int performAction(int currentPosition) {
    System.out.println("Ladder to position: " + endPosition);
    return endPosition;
  }
}
