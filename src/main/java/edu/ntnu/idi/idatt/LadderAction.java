package edu.ntnu.idi.idatt;

public class LadderAction implements TileAction{

  private int destinationTileId;
  private boolean isLadder; //True for up, false for down

  public LadderAction(int destinationTileId){
    this.destinationTileId = destinationTileId;
    this.isLadder = true;
  }

  @Override
  public void perform (Player player) {
    int startTileId = player.getCurrentTile().getTileId();

    isLadder = destinationTileId > startTileId;

    String actionType = isLadder ? "climbs ladder" : "slides down chute";

    System.out.println(player.getName() + " " + actionType + " to tile " + destinationTileId);

    Tile destinationTile = player.getGame().getBoard().getTile(destinationTileId);
    if (destinationTile != null) {
      Tile oldTile = player.getCurrentTile();

      player.placeOnTile(destinationTile);

      GameEvent.EventType eventType = isLadder ?
        GameEvent.EventType.LADDER_CLIMBED :
        GameEvent.EventType.CHUTE_SLID;


      BoardGame game = player.getGame();
      if (game instanceof ObservableGame) {
        ((ObservableGame) game).notifyObservers(
          new GameEvent(eventType, player, oldTile, destinationTile)
        );
      }
    } else {
      throw new IllegalStateException("Destination tile " + destinationTileId + " not found");
    }
  }
}


