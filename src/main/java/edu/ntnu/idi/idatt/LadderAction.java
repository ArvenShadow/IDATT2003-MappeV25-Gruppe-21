package edu.ntnu.idi.idatt;

public class LadderAction implements TileAction{

  private int destinationTileId;

  public LadderAction(int destinationTileId){
    this.destinationTileId = destinationTileId;
  }

  @Override
  public void perform (Player player) {
    System.out.println(player.getName() + " ladders to tile " + destinationTileId);
    Tile destinationTile = player.getGame().getBoard().getTile(destinationTileId);

    if (destinationTile != null) {
      player.placeOnTile(destinationTile);
    } else {
      throw new IllegalStateException("Destination tile " + destinationTileId + " not found");
    }
  }
}


