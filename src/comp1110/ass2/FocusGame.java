package comp1110.ass2;

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
    private State[][] boardState = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK}
    };

    private Tile[][] tilesState = new Tile[8][4]; // same type with the game : col/row

    // initialize the state of the board
    public void initializeBoardState(String boardState) {
        for(int i = 0; i < boardState.length()/4; i ++) {
            String placement = boardState.substring(i*4, (i+1)*4);
            addTileToBoard(placement);
        }

    }

    // method: add a tile into the board
    public void addTileToBoard(String placement) {
        Tile tile = new Tile(placement);
        updateTilesState(tile);
        updateBoardState(tile);
    }

    public void addTilesToTilesState(Tile tile, TileType tileType, Orientation orientation, int locationCol, int locationRow) {
        if(tileType == A || tileType == D || tileType == E || tileType ==G) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 3; i ++) { // i - column
                    for(int j = 0; j < 2; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 2; i ++) { // i - row
                    for(int j = 0; j < 3; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
        }
        if(tileType == B || tileType == C) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 4; i ++) { // i - column
                    for(int j = 0; j < 2; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 2; i ++) { // i - row
                    for(int j = 0; j < 4; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
        }
        if(tileType == H) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 3; i ++) { // i - column
                    for(int j = 0; j < 3; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 3; i ++) { // i - row
                    for(int j = 0; j < 3; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
        }
        if(tileType == I) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 2; i ++) { // i - column
                    for(int j = 0; j < 2; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 2; i ++) { // i - row
                    for(int j = 0; j < 2; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            tilesState[locationCol+i][locationRow+j] = tile;
                        }
                    }
                }
            }
        }
        if(tileType == F) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 3; i ++) { // i - column
                    if(tileType.getOnePointState(tileType, orientation, i, 0) != EMPTY) {
                        tilesState[locationCol+i][locationRow+0] = tile;
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 3; i ++) { // i - row
                    if(tileType.getOnePointState(tileType, orientation, 0, i) != EMPTY) {
                        tilesState[locationCol+0][locationRow+i] = tile;
                    }
                }
            }
        }
    }

    public void addTilesToboardState(Tile tile, TileType tileType, Orientation orientation, int locationCol, int locationRow) {
        if(tileType == A || tileType == D || tileType == E || tileType ==G) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 3; i ++) { // i - column
                    for(int j = 0; j < 2; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 2; i ++) { // i - row
                    for(int j = 0; j < 3; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
        }
        if(tileType == B || tileType == C) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 4; i ++) { // i - column
                    for(int j = 0; j < 2; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 2; i ++) { // i - row
                    for(int j = 0; j < 4; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
        }
        if(tileType == H) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 3; i ++) { // i - column
                    for(int j = 0; j < 3; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 3; i ++) { // i - row
                    for(int j = 0; j < 3; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
        }
        if(tileType == I) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 2; i ++) { // i - column
                    for(int j = 0; j < 2; j ++) { // j - row
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 2; i ++) { // i - row
                    for(int j = 0; j < 2; j ++) { // j - column
                        if(tileType.getOnePointState(tileType, orientation, i, j) != EMPTY) {
                            boardState[locationCol+i][locationRow+j] = tileType.getOnePointState(tileType, orientation, i, j);
                        }
                    }
                }
            }
        }
        if(tileType == F) {
            if (orientation == NORTH || orientation == SOUTH) {
                for(int i = 0; i < 3; i ++) { // i - column
                    if(tileType.getOnePointState(tileType, orientation, i, 0) != EMPTY) {
                        boardState[locationCol+i][locationRow+0] = tileType.getOnePointState(tileType, orientation, i, 0);
                    }
                }
            }
            if (orientation == EAST || orientation == WEST) {
                for(int i = 0; i < 3; i ++) { // i - row
                    if(tileType.getOnePointState(tileType, orientation, 0, i) != EMPTY) {
                        boardState[locationCol+0][locationRow+i] = tileType.getOnePointState(tileType, orientation, 0, i);
                    }
                }
            }
        }
    }

    // update the tiles state
    public void updateTilesState(Tile tile) {
        TileType tileType = tile.getTileType();
        Location location = tile.getLocation();
        int locationCol = location.getCol();
        int locationRow = location.getRow();
        Orientation orientation = tile.getOrientation();
        addTilesToTilesState(tile, tileType, orientation, locationCol, locationRow);
    }

    // update the board state
    public void updateBoardState(Tile tile) {
        TileType tileType = tile.getTileType();
        Location location = tile.getLocation();
        int locationCol = location.getCol();
        int locationRow = location.getRow();
        Orientation orientation = tile.getOrientation();
        addTilesToboardState(tile, tileType, orientation, locationCol, locationRow);
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
        if (!isPlacementStringWellFormed(placement))
            return false;
        for(int i = 0; i < placement.length()/4; i ++) {
            String piecePlacement = placement.substring(i*4, (i+1)*4);
            Tile tile = new Tile(piecePlacement);
            TileType tileType = tile.getTileType();
            Orientation orientation = tile.getOrientation();
            int locationCol = tile.getLocation().getCol();
            int locationRow = tile.getLocation().getRow();
            if (!isPiecePlacementWellFormed(piecePlacement))
                return false;
            if(tileType == A || tileType == D || tileType == E || tileType == G) {
                if(orientation == NORTH || orientation == SOUTH) {
                    if(locationCol + 3 > 9 || locationRow + 2 > 5) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 3 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+1) != EMPTY)
                        return false;
                    if(locationCol == 6 && locationRow == 3 && tileType.getOnePointState(tileType, orientation, locationCol+2, locationRow+1) != EMPTY)
                        return false;
                }
                if(orientation == EAST || orientation == WEST) {
                    if(locationCol + 2 > 9 || locationRow + 3 > 5) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 2 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+2) != EMPTY)
                        return false;
                    if(locationCol == 7 && locationRow == 2 && tileType.getOnePointState(tileType, orientation, locationCol+2, locationRow+1) != EMPTY)
                        return false;
                }
            }
            if(tileType == B || tileType == C || tileType == J) {
                if(orientation == NORTH || orientation == SOUTH) {
                    if(locationCol + 4 > 9 || locationRow + 2 > 5) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 3 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+1) != EMPTY)
                        return false;
                    if(locationCol == 5 && locationRow == 3 && tileType.getOnePointState(tileType, orientation, locationCol+3, locationRow+1) != EMPTY)
                        return false;
                }
                if(orientation == EAST || orientation == WEST) {
                    if(locationCol + 2 > 9 || locationRow + 4 > 5) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 1 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+3) != EMPTY)
                        return false;
                    if(locationCol == 7 && locationRow == 1 && tileType.getOnePointState(tileType, orientation, locationCol+1, locationRow+3) != EMPTY)
                        return false;
                }
            }
            if(tileType == H) {
                if(orientation == NORTH || orientation == SOUTH) {
                    if(locationCol + 3 > 8 || locationRow + 3 > 4) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 2 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+2) != EMPTY)
                        return false;
                    if(locationCol == 6 && locationRow == 2 && tileType.getOnePointState(tileType, orientation, locationCol+2, locationRow+2) != EMPTY)
                        return false;
                }
                if(orientation == EAST || orientation == WEST) {
                    if(locationCol + 3 > 8 || locationRow + 3 > 4) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 2 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+2) != EMPTY)
                        return false;
                    if(locationCol == 6 && locationRow == 2 && tileType.getOnePointState(tileType, orientation, locationCol+2, locationRow+2) != EMPTY)
                        return false;
                }
            }
            if(tileType == I) {
                if(orientation == NORTH || orientation == SOUTH) {
                    if(locationCol + 2 > 8 || locationRow + 2 > 4) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 3)
                        return false;
                    if(locationCol == 7 && locationRow == 3)
                        return false;
                }
                if(orientation == EAST || orientation == WEST) {
                    if(locationCol + 2 > 8 || locationRow + 2 > 4) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 3 && tileType.getOnePointState(tileType, orientation, locationCol, locationRow+1) != EMPTY)
                        return false;
                    if(locationCol == 7 && locationRow == 3 && tileType.getOnePointState(tileType, orientation, locationCol+1, locationRow+1) != EMPTY)
                        return false;
                }
            }
            if(tileType == F) {
                if(orientation == NORTH || orientation == SOUTH) {
                    if(locationCol + 3 > 8) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 4)
                        return false;
                    if(locationCol == 6 && locationRow == 4)
                        return false;
                }
                if(orientation == EAST || orientation == WEST) {
                    if(locationRow + 3 > 4) {
                        return false;
                    }
                    if(locationCol == 0 && locationRow == 2)
                        return false;
                    if(locationCol == 8 && locationRow == 2)
                        return false;
                }
            }
        }

        return true;
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
        return null;
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
