package comp1110.ass2;



import java.util.*;

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

    /**
     * author: Jianwu Yao u6987162 (task2 and task3 were solved by Zheyuan Zhang)
     * first part from line25 to line 63
     */

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

    //just to show the state of the one point of the board
    public State printPointState(int col, int row) {
        return boardStates[row][col];
    }

    //just to show all state of the points of the board
    public void printBoardStates() {
        for(int i = 0; i < 5; ++ i) {
            for(int j = 0; j < 9; ++ j) {
                System.out.print(boardStates[i][j] + " ");
            }
            System.out.println();
        }
    }

    //just to show the tilesStates's state
    public void printTilesStates() {
        for(int i = 0; i < 4; ++ i) {
            for(int j = 0; j < 8; ++ j) {
                System.out.print(tilesState[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * author: Jianwu Yao u6987162 from line 78 to line 191
     * initialize the state of the board
     * @param boardState :
     * @param ifchallenge :
     *      - when ifchallenge is false, we check the placement and put tiles on the borad i.e. we would change the board states;
     *      - otherwise, we would just check the qualification of the tiles but not put the tile on the board.
     * @param testCol
     *      - this parameter is the column value of the point we need to put a tile on it
     * @param testRow
     *      - this parameter is the row value of the point we need to put a tile on it
     * @return
     */
    public boolean initializeBoardState(String boardState, boolean ifchallenge, int testCol, int testRow) {
        for(int i = 0; i < boardState.length()/4; i ++) {
            String placement = boardState.substring(i*4, (i+1)*4);
            if(!addTileToBoard(placement, ifchallenge, testCol, testRow))
                return false;
        }
        return true;

    }

    // add a tile into the board and destrcture the placement to Tile type
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
     * @return True if the piece placement is well-formed\
     *
     *
     * author Zheyuan Zhang u6870923 line 189 - line 201
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
     *
     * author Zheyuan Zhang u6870923 line 214 - line 238
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

    /**
     * task 5
     * author: Jianwu Yao u6987162
     * comment: at this task, I mainly use the method defined from line 79 to line 191 to fix this problem
     * @param placement
     * @return
     */
    public static boolean isPlacementStringValid(String placement) {
        // FIXME Task 5: determine whether a placement string is valid
        FocusGame focusGame = new FocusGame();
        if(isPlacementStringWellFormed(placement))
            return focusGame.initializeBoardState(placement, false, 0, 0);
        return false;
    }

    /**
     * Given a string describing a placement of pieces and a string describing
     * a challenge, return a set of all possible next viable piece placements
     * which cover a specific board location.
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
     * @param col      The location's column.
     * @param row      The location's row.
     * @return A set of viable piece placements, or null if there are none.
     */

    /**
     * task6
     * author: Jianwu Yao u6987162
     * from line 324 to line 431
     * @param placement
     * @param challenge
     * @param col
     * @param row
     * @return
     */
    public static Set<String> getViablePiecePlacements(String placement, String challenge, int col, int row) {
        // FIXME Task 6: determine the set of all viable piece placements given existing placements and a challenge

        int testCol = col;
        int testRow = row;

        FocusGame focusGame = new FocusGame();

        // initialize the board state
        focusGame.initializeBoardState(placement, false, 0, 0);

        /**
         * to do some work to prepare for possible solution(placement type)
         * 1-the tile type
         * 2-the tile orientation
         * I think the most important part is to get the possible left-up point including the row value and column value of the point
         */
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
        // add possible points (focus on the left-up point)
        for(int i = 0; i < 4 && testCol - i >= 0; ++ i) {
            for(int j = 0; j < 4 && testRow - j >= 0; ++ j) {
                StringBuilder res = new StringBuilder();
                res.append(testCol-i).append(testRow-j);
                possiblePoints.add(res.toString());
            }
        }

        focusGame.challengeBoardStates(focusGame, challenge);

        // to remove the tile almost in the inout placement which is on the board
        for(int i = 0; i < placement.length()/4; i ++) {
            String piecePlacement = placement.substring(i * 4, (i + 1) * 4);
            String usedtype = Character.toString(piecePlacement.charAt(0));
            if (typeList.contains(usedtype))
                typeList.remove(usedtype);
        }

        // construct the possible solution which will put on the board
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

        // to check the qualification of the tiles imagining we put it on the board
        for(String possibleSolution : possibleSolutions) {
            if (focusGame.initializeBoardState(possibleSolution, true, testCol, testRow)) {
                solutionSet.add(possibleSolution);
            }

        }

        return (solutionSet.size() != 0) ? solutionSet : null;
    }

    // according to the challenge string to change the central nine points correspondingly
    public void challengeBoardStates(FocusGame focusGame, String challenge) {
        int iter = 0;
        for(int i = 1; i < 4; ++ i) {
            for(int j = 3; j < 6; ++ j) {
                focusGame.boardStates[i][j] = charToState(challenge.charAt(iter));
                iter ++;
            }
        }
    }

    // transfer from the char to the state correspondingly
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
     * @param //challenge A challenge string.
     * @return A placement string describing a canonical encoding of the solution to
     * the challenge.
     */

    /**
     * task9
     * author: Jianwu Yao
     * from line 456 to line 845
     */

    // transfer the Set to the String type just like to get the full String from the set
    static String stringSetToString(Set<String> stringSet) {
        if(stringSet.size() == 0)
            return null;
        StringBuilder sb = new StringBuilder();
        for(String str: stringSet) {
            sb.append(str);
        }
        return sb.toString();
    }

    // checkValidAround and checlValidBoardState was designed to exclude the situation of worry placement as earlier as possible
    // but I found may be it did lettle help
    public boolean checkValidAround(int col, int row) {
        ArrayList<Boolean> checkBooleanList = new ArrayList<>();
        //|| row - 1 < 0 || col + 1 > 9 || row + 1 > 4
//        State left = boardStates[row][col-1];
//        State right = boardStates[row][col+1];
//        State up = boardStates[row-1][col];
//        State down = boardStates[row+1][col];

        State left = RED;
        State right = RED;
        State up = RED;
        State down = RED;

        if(col - 1 < 0 && row - 1 < 0) {
            right = boardStates[row][col+1];
            down = boardStates[row+1][col];
        } else if(col + 1 > 8 && row - 1 < 0) {
            left = boardStates[row][col-1];
            down = boardStates[row+1][col];
        } else if(col - 1 < 0) {
            up = boardStates[row-1][col];
            right = boardStates[row][col+1];
            down = boardStates[row+1][col];
        } else if(row - 1 < 0) {
            left = boardStates[row][col-1];
            right = boardStates[row][col+1];
            down = boardStates[row+1][col];
        } else if(col + 1 > 8) {
            up = boardStates[row-1][col];
            left = boardStates[row][col-1];
            down = boardStates[row+1][col];
        } else if(row + 1 > 4) {
            left = boardStates[row][col-1];
            right = boardStates[row][col+1];
            up = boardStates[row-1][col];
        } else {
            left = boardStates[row][col-1];
            right = boardStates[row][col+1];
            up = boardStates[row-1][col];
            down = boardStates[row+1][col];
        }


        if(col - 1 < 0 && row - 1 < 0) {
            if(right != EMPTY)
                checkBooleanList.add(false);
            if(down != EMPTY)
                checkBooleanList.add(false);
        }
        else if(col + 1 > 9 && row - 1 < 0) {
            if(left != EMPTY)
                checkBooleanList.add(false);
            if(down != EMPTY)
                checkBooleanList.add(false);
        }
        else if(col - 1 < 0) {
            checkBooleanList.add(false);
            if(up != EMPTY)
                checkBooleanList.add(false);
            if(right != EMPTY)
                checkBooleanList.add(false);
            if(down != EMPTY)
                checkBooleanList.add(false);
        }
        else if(row - 1 < 0) {
            checkBooleanList.add(false);
            if(left != EMPTY)
                checkBooleanList.add(false);
            if(right != EMPTY)
                checkBooleanList.add(false);
            if(down != EMPTY)
                checkBooleanList.add(false);
        }
        else if(col + 1 > 9) {
            checkBooleanList.add(false);
            if(up != EMPTY)
                checkBooleanList.add(false);
            if(right != EMPTY)
                checkBooleanList.add(false);
            if(down != EMPTY)
                checkBooleanList.add(false);
        }
        else if(row + 1 > 4) {
            checkBooleanList.add(false);
            if(left != EMPTY)
                checkBooleanList.add(false);
            if(right != EMPTY)
                checkBooleanList.add(false);
            if(up != EMPTY)
                checkBooleanList.add(false);
        }
        else {
            if(left != EMPTY )
                checkBooleanList.add(false);
            if(right != EMPTY )
                checkBooleanList.add(false);
            if(up != EMPTY )
                checkBooleanList.add(false);
            if(down != EMPTY )
                checkBooleanList.add(false);
        }

        if(checkBooleanList.size() == 4)
            return false;
        return true;
    }

    public boolean checkValidBoardState(String placement) {
        FocusGame focusGame = new FocusGame();
        focusGame.initializeBoardState(placement, false, 0, 0);
        for(int i = 0; i < 5; ++ i) {
            for(int j = 0; j < 9; ++ j) {
                if(i == 4 && j == 0 || i == 4 && j == 8)
                    continue;
                if(focusGame.printPointState(j, i) != EMPTY)
                    continue;
                if(!checkValidAround(j, i)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }

    // getAllNineCentralPointsSolution focus on solving the central nine points firstly
    static Set<String> getAllNineCentralPointsSolution(String challenge) {
        Set<String> triggerStrings = new HashSet<>();
        triggerStrings = getViablePiecePlacements("", challenge, 3, 1);
        Set<Set<String>> allNineSolutions = new HashSet<>();
        for(String triggerString: triggerStrings) {
            Set<String> middleArrayList = new HashSet<>();
            middleArrayList.add(triggerString);
            allNineSolutions.add(middleArrayList);
        }

        /**
         * this garbageSet is very important
         * it works as collecting the worry possibilities and excluding the old way and worry way timely
         */
        Set<Set<String>> garbageSet = new HashSet<>();

        for(int i = 3; i < 6; ++ i) {
            for (int j = 1; j < 4; ++j) {
                if(i == 3 && j == 1)
                    continue;

                /**
                 * oldAllNineSolutuons is very important
                 * because the reference mechanism we need to use old String set but not change its state so we can go ahead
                 * we use addAll method to low copy the former solution set to achieve our idea
                 */
                Set<Set<String>> oldAllNineSolutuons = new HashSet<>();
                oldAllNineSolutuons.addAll(allNineSolutions);

                for(Set setString: oldAllNineSolutuons) {

                    String placement = stringSetToString(setString);

                    FocusGame focusGame = new FocusGame();
                    focusGame.initializeBoardState(placement, false, 0, 0);

                    if(focusGame.printPointState(i, j) != EMPTY) {
                        continue;
                    }

                    Set<String> tmpSet = new HashSet<>();
                    tmpSet = getViablePiecePlacements(placement, challenge, i, j);

                    garbageSet.add(setString);

                    if(tmpSet == null) {
                        continue;
                    }

                    for(String str: tmpSet) {

                        Set<String> laterSet = new HashSet<>();
                        laterSet.add(placement);
                        laterSet.add(str);

                        allNineSolutions.add(laterSet);
                    }
                    allNineSolutions.removeAll(garbageSet);
                }

            }
        }

        Set<String> allNineSolutionStringStyle = new HashSet<>();
        for(Set item: allNineSolutions) {
            allNineSolutionStringStyle.add(stringSetToString(item));
        }
        return allNineSolutionStringStyle;
    }

    /**
     * the getSolution method's idea is just like getAllNineCentralPointsSolution method but we need to precess the final result and
     * other conditions
     * @param challenge
     * @return
     */
    public static String getSolution(String challenge) {
        // FIXME Task 9: determine the solution to the game, given a particular challenge

        // get possible solutions from the method beyond
        Set<String> possibleSolutionsForCentralNine = getAllNineCentralPointsSolution(challenge);

        for(String strFromNineSolutions: possibleSolutionsForCentralNine) {

            FocusGame focusGame = new FocusGame();
            //initialize
            focusGame.initializeBoardState(strFromNineSolutions, false, 0, 0);

//            if(!focusGame.checkValidBoardState(strFromNineSolutions))
//                continue;

            Set<String> outsideString = new HashSet<>();

            // to get the first tile's possibilities
            if(outsideString.size() == 0) {

                outsideString = getViablePiecePlacements(strFromNineSolutions, challenge, 0, 0);
                Set<String> outsideStringUpdate = new HashSet<>();
                if(outsideString == null)
                    continue;

                for (String stringFirst : outsideString) {
//                    if(!focusGame.checkValidBoardState(strFromNineSolutions+stringFirst))
//                        continue;
                    outsideStringUpdate.add(strFromNineSolutions + stringFirst);
                }

                outsideString.clear();
                outsideString.addAll(outsideStringUpdate);
            }

            for(int i = 0; i < 5; ++i) {
                for(int j = 0; j < 9; ++j) {
                    if (i == 4 && j == 0 || i == 4 && j == 8)
                        continue;
                    if (focusGame.printPointState(j, i) != EMPTY)
                        continue;

                    /**
                     * this garbageSet is very important
                     * it works as collecting the worry possibilities and excluding the old way and worry way timely
                     */
                    Set<String> garbageSet = new HashSet<>();
                    Set<String> outsideStringUpdate = new HashSet<>();

                    for (String outsidestring : outsideString) {

                        Set<String> resultStringSet = getViablePiecePlacements(outsidestring, challenge, j, i);
                        if (resultStringSet == null)
                            continue;
                        garbageSet.add(outsidestring);

                        for (String resultString : resultStringSet) {
                            if (!focusGame.checkValidBoardState(outsidestring + resultString))
                                continue;

                            outsideStringUpdate.add(outsidestring + resultString);
                        }
                    }
                    outsideString.removeAll(garbageSet);
                    outsideString.addAll(outsideStringUpdate);

                    }

                }
                ArrayList<String> solutionList = new ArrayList<>();
                if(outsideString.size() == 0)
                    continue;
                else {
                    for(String outsidestring : outsideString) {
                        if(outsidestring.length() != 40)
                            continue;
                        else {
                            solutionList.add(outsidestring);
                        }
                    }
                    if(solutionList.size() != 0) {

                        Collections.sort(solutionList);
                        String stringProcessed = solutionList.get(0);
                        int indexOfF = stringProcessed.indexOf('f');
                        int indexOfG = stringProcessed.indexOf('g');

                        StringBuilder res = new StringBuilder(stringProcessed);
                        if(stringProcessed.charAt(indexOfF+3) == '3')
                            res.setCharAt(indexOfF+3, '1');
                        if(stringProcessed.charAt(indexOfF+3) == '2')
                            res.setCharAt(indexOfF+3, '0');
                        if(stringProcessed.charAt(indexOfG+3) == '2')
                            res.setCharAt(indexOfG+3,'0');
                        if(stringProcessed.charAt(indexOfG+3) == '3')
                            res.setCharAt(indexOfG+3,'1');
                        return res.toString();
                    }

                    continue;

                }
            }
        return null;
    }

    /**
     * this version is because the old way when I solve this problem, I always found the boarder solution set
     * just like the different between depth-first search and width-first search.
     * but because the time limit
     * recursive version for task9
     */
    static boolean checkCentralNine(String placement) {
        FocusGame focusGame = new FocusGame();
        focusGame.initializeBoardState(placement, false, 0, 0);
        for(int row = 1; row < 4; ++ row) {
            for(int col = 3; col < 6; ++ col) {
                if(focusGame.printPointState(col, row) == EMPTY)
                    return false;
            }
        }
        return true;
    }

    static String getOneTileAtOnePoint(String challenge, String placement, int testRow, int testCol) {
//        FocusGame focusGame = new FocusGame();
//        focusGame.initializeBoardState(placement, false, 0,0);
//
//        ArrayList<String> typeList = new ArrayList<>() {{
//            add("a");
//            add("b");
//            add("c");
//            add("d");
//            add("e");
//            add("f");
//            add("g");
//            add("h");
//            add("i");
//            add("j");
//        }};
//        ArrayList<Integer> orientationList = new ArrayList<>() {{
//            add(0);
//            add(1);
//            add(2);
//            add(3);
//        }};
//
//        ArrayList<String> possiblePoints = new ArrayList<>();
//
//        for(int i = 0; i < 4 && testCol - i >= 0; ++ i) {
//            for(int j = 0; j < 4 && testRow - j >= 0; ++ j) {
//                StringBuilder res = new StringBuilder();
//                res.append(testCol-i).append(testRow-j);
//                possiblePoints.add(res.toString());
//            }
//        }


        return null;
    }

    public static String findCentralNineString(String challenge) {

        String placement = null;
        if(checkCentralNine(placement))
            return placement;
        else
            return placement + findNextOne(placement);
    }

    public static String findNextOne(String placement) {
        if(checkCentralNine(placement))
            return placement;
        else {

            return placement + findNextOne(placement);
        }
    }
}


