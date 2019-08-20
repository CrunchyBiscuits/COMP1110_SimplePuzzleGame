package comp1110.ass2;

/**
 * get and update the board state
 * @param placement
 */
public class BoardAndTilesState {
    private String placement;

    public BoardAndTilesState(String placement) {
        this.placement = placement;
    }

    // pre-initial state of the board
    private State[][] boradState = {

    };

    private Tile[][] tilesState = new Tile[9][5]; // same type with the game : col/row

    // initialize the state of the board
    public void initializeBoardState(String placement) {
        addTileToBoard(placement);
    }

    // method: add a tile into the board
    public void addTileToBoard(String placement) {
        Tile tile = new Tile(placement);

        updateTilesState(tile);
        updateBoardState(tile);
    }

    // update the tiles state
    public void updateTilesState(Tile tile) {
        //
    }

    // update the board state
    public void updateBoardState(Tile tile) {
        //
    }
}
