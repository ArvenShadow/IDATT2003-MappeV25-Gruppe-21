package edu.ntnu.idi.idatt;



public class Tile {

  private int tileId;
  private Tile nextTile;

  private TileAction tileAction;

  public Tile(int tileId){
    this.tileId = tileId;
    this.nextTile = null;
  }

  public int getTileId(){
    return tileId;
  }

  public Tile getNextTile(){
    return nextTile;
  }

  public void setNextTile(Tile nextTile){
    this.nextTile = nextTile;
  }

  public void setTileAction(TileAction tileAction){
    this.tileAction = tileAction;
  }

  public void landAction(Player player) {
    if (tileAction != null) {
      tileAction.perform(player);
    } else {
      System.out.println("No action for tile " + tileId);
    }
  }

}
