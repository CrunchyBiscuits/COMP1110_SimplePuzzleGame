package comp1110.ass2;

import java.util.ArrayList;
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

    private boolean ifchallenge = false;
    private int testCol = 0;
    private int testRow = 0;

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
    public boolean initializeBoardState(String boardState, boolean ifchallenge, int testCol, int testRow) {
        for(int i = 0; i < boardState.length()/4; i ++) {
            String placement = boardState.substring(i*4, (i+1)*4);
            if(!addTileToBoard(placement, ifchallenge, testCol, testRow))
                return false;
        }
        return true;

    }

    // method: add a tile into the board
    public boolean addTileToBoard(String placement, boolean ifchallenge, int testCol, int testRow) {
        Tile tile = new Tile(placement);
        return updateBoardStateAndTilesState(tile, ifchallenge, testCol, testRow);
    }

    public boolean updateBoardStateAndTilesState(Tile tile, boolean ifchallenge, int testCol, int testRow) {
        TileType tileType = tile.getTileType();
        Location location = tile.getLocation();
        int locationCol = location.getCol();
        int locationRow = location.getRow();

        Orientation orientation = tile.getOrientation();
        return addTilesToTilesStateAndBoardState(tile, tileType, orientation, locationCol, locationRow, ifchallenge, testCol, testRow);
    }

    public boolean addTilesToTilesStateAndBoardState(Tile tile, TileType tileType, Orientation orientation, int locationCol, int locationRow, boolean ifchallenge, int testCol, int testRow) {
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
        return checkAndChangeStatesTiles(tile, tileType, row, col, orientation, locationCol, locationRow, ifchallenge, testCol, testRow);
    }

    public boolean checkAndChangeStatesTiles(Tile tile, TileType tileType, int row, int col, Orientation orientation, int locationCol, int locationRow, boolean ifchallenge, int testCol, int testRow) {

        if(orientation == EAST || orientation == WEST) {
            int temp = row;
            row = col;
            col = temp;
        }
        
        int count = 0;
        for(int i = 0; i < col; ++i) {
            for(int j = 0; j < row; ++j) {
                if(locationRow+j > 4 || locationCol+i > 8)
                    return false;
                State tilePointState = tileType.getOnePointState(tileType, orientation, i, j);
                if(ifchallenge) {

                    if(locationCol+i == testCol && locationRow+j == testRow && tilePointState == EMPTY) {
                        return false;
                    }


                    if(!(locationCol+i == testCol && locationRow+j == testRow))
                        count ++;

                    if(tilePointState != EMPTY && tilesState[locationRow+j][locationCol+i] != null)
                        return false;

                    if(locationRow+j >= 1 && locationRow+j <= 3 && locationCol+i >= 3 && locationCol+i <= 5) {
                        if(tilePointState != EMPTY && tilePointState != boardStates[locationRow+j][locationCol+i]) {
                            return false;
                        }
                    } else {
                        if(tilePointState != EMPTY && boardStates[locationRow+j][locationCol+i] != BLOCK) {
                            if(boardStates[locationRow+j][locationCol+i] != EMPTY) {
                                return false;
                            }
                        } else if(tilePointState != EMPTY && boardStates[locationRow+j][locationCol+i] == BLOCK) {
                            return false;
                        }
                    }

                } else {
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
        }

        if(ifchallenge  && count == col*row) {
            return false;
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
//        if(placement.)
//        // FIXME Task 5: determine whether a placement string is valid
//        FocusGame focusGame = new FocusGame();
//        if(isPlacementStringWellFormed(placement))
//            return focusGame.initializeBoardState(placement, false, 0, 0);
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
     * @param //placement A viable placement string
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
     * @param //col      The cell's column.
     * @param //row      The cell's row.
     * @return A set of viable piece placements, or null if there are none.
     */


    public void challengeBoardStates(FocusGame focusGame, String challenge) {
        int iter = 0;
        for(int i = 1; i < 4; ++ i) {
            for(int j = 3; j < 6; ++ j) {
                focusGame.boardStates[i][j] = charToState(challenge.charAt(iter));
                //focusGame.tilesState[i][j] = new Tile("a000");
                iter ++;
            }
        }
    }

    public State charToState(char ch) {
        String str = Character.toString(ch);
        switch (str) {
            case "R":
                return RED;
            case "B":
                return BLUE;
            case "W":
                return WHITE;
            case "G":
                return GREEN;
        }
        return RED;
    }

    static Set<String> getViablePiecePlacements(String placement, String challenge, int col, int row) {
        // FIXME Task 6: determine the set of all viable piece placements given existing placements and a challenge

        int testCol = col;
        int testRow = row;


        FocusGame focusGame = new FocusGame();
        focusGame.initializeBoardState(placement, false, 0, 0);


        ArrayList<String> typeList = new ArrayList<>() {{
            add("a");
            add("b");
            add("c");
            add("d");
            add("e");
            add("f");
            add("g");
            add("h");
            add("i");
            add("j");
        }};
        ArrayList<Integer> orientationList = new ArrayList<>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};

        ArrayList<String> possiblePoints = new ArrayList<>();
        // add possible points
        for(int i = 0; i < 4 && testCol - i >= 0; ++ i) {
            for(int j = 0; j < 4 && testRow - j >= 0; ++ j) {
                StringBuilder res = new StringBuilder();
                res.append(testCol-i).append(testRow-j);
                possiblePoints.add(res.toString());
            }
        }

        focusGame.challengeBoardStates(focusGame, challenge);

        for(int i = 0; i < placement.length()/4; i ++) {
            String piecePlacement = placement.substring(i * 4, (i + 1) * 4);
            String usedtype = Character.toString(piecePlacement.charAt(0));
            if (typeList.contains(usedtype))
                typeList.remove(usedtype);
        }
        ArrayList<String> possibleSolutions = new ArrayList<>();
        for(String type : typeList) {
            for(Integer orien : orientationList) {
                for(String possiblePoint : possiblePoints) {
                    StringBuilder res = new StringBuilder();
                    res.append(type).append(possiblePoint).append(orien);
                    possibleSolutions.add(res.toString());
                }
            }
        }

        Set<String> solutionSet = new HashSet<>();

        for(String possibleSolution : possibleSolutions) {
            if (focusGame.initializeBoardState(possibleSolution, true, testCol, testRow)) {
                solutionSet.add(possibleSolution);
            }

        }

        return (solutionSet.size() != 0) ? solutionSet : null;
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
