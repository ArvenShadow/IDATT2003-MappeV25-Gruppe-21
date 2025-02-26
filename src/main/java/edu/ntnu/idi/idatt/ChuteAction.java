package edu.ntnu.idi.idatt;

public class ChuteAction implements TileAction{
  private int destinationTileId;

  public ChuteAction(int destinationTileId){
    this.destinationTileId = destinationTileId;
  }

  @Override
  public void perform (Player player) {
    System.out.println(player.getName() + " chutes to tile " + destinationTileId);
    player.setCurrentTileID(destinationTileId);
  }
}
