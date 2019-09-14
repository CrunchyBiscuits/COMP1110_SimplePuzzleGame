package comp1110.ass2;

import java.util.HashSet;
import java.util.Set;

import static comp1110.ass2.TileType.*;
import static comp1110.ass2.Orientation.*;
import static comp1110.ass2.State.*;


/**
 * This class provides the text interface for the IQ Focus Game
 * <p>
 * The game is based directly on Smart Games' IQ-Focus game
 * (https://www.smartgames.eu/uk/one-player-games/iq-focus)
 */
public class FocusGame {


    // pre-initial state of the board
    private State[][] boardStates = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK}
    };

    private Tile[][] tilesState = new Tile[5][9]; // same type with the game : col/row

    public void printBoardStates() {
        for(int i = 0; i < 5; ++ i) {
            for(int j = 0; j < 9; ++ j) {
                System.out.print(boardStates[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printTilesStates() {
        for(int i = 0; i < 4; ++ i) {
            for(int j = 0; j < 8; ++ j) {
                System.out.print(tilesState[i][j] + " ");
            }
            System.out.println();
        }
    }

    // initialize the state of the board
    public boolean initializeBoardState(String boardState) {
        for(int i = 0; i < boardState.length()/4; i ++) {
            String placement = boardState.substring(i*4, (i+1)*4);
            if(!addTileToBoard(placement))
                return false;
        }
        return true;
    }

    // method: add a tile into the board
    public boolean addTileToBoard(String placement) {
        Tile tile = new Tile(placement);
        return updateBoardStateAndTilesState(tile);
    }

    public boolean updateBoardStateAndTilesState(Tile tile) {
        TileType tileType = tile.getTileType();
        Location location = tile.getLocation();
        int locationCol = location.getCol();
        int locationRow = location.getRow();
        Orientation orientation = tile.getOrientation();
        return addTilesToTilesStateAndBoardState(tile, tileType, orientation, locationCol, locationRow);
    }

    public boolean addTilesToTilesStateAndBoardState(Tile tile, TileType tileType, Orientation orientation, int locationCol, int locationRow) {
        int row = 0;
        int col = 0;
        if(tileType == A || tileType == D || tileType == E || tileType ==G) {
            row = 2;
            col = 3;
        }
        if(tileType == B || tileType == C || tileType == J) {
            row = 2;
            col = 4;
        }
        if(tileType == H) {
            row = 3;
            col = 3;
        }
        if(tileType == I) {
            row = 2;
            col = 2;
        }
        if(tileType == F) {
            row = 1;
            col = 3;
        }
        return checkAndChangeStatesTiles(tile, tileType, row, col, orientation, locationCol, locationRow);
    }

    public boolean checkAndChangeStatesTiles(Tile tile, TileType tileType, int row, int col, Orientation orientation, int locationCol, int locationRow) {

        if(orientation == EAST || orientation == WEST) {
            int temp = row;
            row = col;
            col = temp;
        }
        for(int i = 0; i < col; ++i) {
            for(int j = 0; j < row; ++j) {
                //check whether the tile is out of board
                if(locationRow+j > 4 || locationCol+i > 8)
                    return false;
                State tilePointState = tileType.getOnePointState(tileType, orientation, i, j);
                //check whether the tile is overlap or placed in the block
                if(tilePointState != EMPTY && boardStates[locationRow+j][locationCol+i] != BLOCK) {
                    if(tilesState[locationRow+j][locationCol+i] == null)
                        tilesState[locationRow+j][locationCol+i] = tile;
                    else
                        return false;

                    boardStates[locationRow+j][locationCol+i] = tilePointState;
                } else if(tilePointState != EMPTY && boardStates[locationRow+j][locationCol+i] == BLOCK) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Determine whether a piece placement is well-formed according to the
     * following criteria:
     * - it consists of exactly four characters
     * - the first character is in the range a .. j (shape)
     * - the second character is in the range 0 .. 8 (column)
     * - the third character is in the range 0 .. 4 (row)
     * - the fourth character is in the range 0 .. 3 (orientation)
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // FIXME Task 2: determine whether a piece placement is well-formed
        if (piecePlacement.length()!=4)
            return false;
        else {
            char tile = piecePlacement.charAt(0);
            char col = piecePlacement.charAt(1);
            char row = piecePlacement.charAt(2);
            char ori = piecePlacement.charAt(3);

            return (tile>='a'&&tile<='j')&&(col>='0'&&col<='8')&&(row>='0'&&row<='4')&&(ori>='0'&&ori<='3');
        }
    }

    /**
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N four-character piece placements (where N = 1 .. 10);
     * - each piece placement is well-formed
     * - no shape appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementStringWellFormed(String placement) {
        // FIXME Task 3: determine whether a placement is well-formed
        if (placement.length()%4!=0||placement=="")
            return false;
        else {
            int pieceNum = placement.length()/4;
            String[] pieceplacements = new String[pieceNum];
            for (int i=0;i<pieceNum;i++){
                pieceplacements[i]=placement.substring(i*4,i*4+4);
            }
            for (int i=0;i<pieceNum;i++){
                if (!isPiecePlacementWellFormed(pieceplacements[i]))
                    return false;
                else {
                    for (int j=0;j<pieceNum;j++){
                        if (j==i)
                            continue;
                        if (pieceplacements[i].charAt(0)==pieceplacements[j].charAt(0))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determine whether a placement string is valid.
     *
     * To be valid, the placement string must be:
     * - well-formed, and
     * - each piece placement must be a valid placement according to the
     *   rules of the game:
     *   - pieces must be entirely on the board
     *   - pieces must not overlap each other
     *
     * @param placement A placement string
     * @return True if the placement sequence is valid
     */
    public static boolean isPlacementStringValid(String placement) {
        // FIXME Task 5: determine whether a placement string is valid
        FocusGame focusGame = new FocusGame();
        if(isPlacementStringWellFormed(placement))
            return focusGame.initializeBoardState(placement);
        return false;
    }

    /**
     * Given a string describing a placement of pieces and a string describing
     * a challenge, return a set of all possible next viable piece placements
     * which cover a specific board cell.
     *
     * For a piece placement to be viable
     * - it must be valid
     * - it must be consistent with the challenge
     *
     * @param placement A viable placement string
     * @param challenge The game's challenge is represented as a 9-character string
     *                  which represents the color of the 3*3 central board area
     *                  squares indexed as follows:
     *                  [0] [1] [2]
     *                  [3] [4] [5]
     *                  [6] [7] [8]
     *                  each character may be any of
     *                  - 'R' = RED square
     *                  - 'B' = Blue square
     *                  - 'G' = Green square
     *                  - 'W' = White square
     * @param col      The cell's column.
     * @param row      The cell's row.
     * @return A set of viable piece placements, or null if there are none.
     */
    static Set<String> getViablePiecePlacements(String placement, String challenge, int col, int row) {
        // FIXME Task 6: determine the set of all viable piece placements given existing placements and a challenge
        Set<String> result=new HashSet<>();
        if (isPiecePlacementWellFormed(placement))
            return null;
        if (isPlacementStringValid(placement))
            return null;

        return result;
    }

    /**
     * Return the canonical encoding of the solution to a particular challenge.
     *
     * A given challenge can only solved with a single placement of pieces.
     *
     * Since some piece placements can be described two ways (due to symmetry),
     * you need to use a canonical encoding of the placement, which means you
     * must:
     * - Order the placement sequence by piece IDs
     * - If a piece exhibits rotational symmetry, only return the lowest
     *   orientation value (0 or 1)
     *
     * @param challenge A challenge string.
     * @return A placement string describing a canonical encoding of the solution to
     * the challenge.
     */
    public static String getSolution(String challenge) {
        // FIXME Task 9: determine the solution to the game, given a particular challenge
        return null;
    }
}
