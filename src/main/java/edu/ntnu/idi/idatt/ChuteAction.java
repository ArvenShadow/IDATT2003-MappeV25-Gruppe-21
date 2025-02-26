package edu.ntnu.idi.idatt;

public class ChuteAction implements TileAction{
  private int endPosition;

  public ChuteAction(int endPosition){
    this.endPosition = endPosition;
  }

  @Override
  public int performAction (int currentPosition) {
    System.out.println("Chute to position: " + endPosition);
    return endPosition;
  }
}
