package comp1110.ass2;

import comp1110.ass2.FocusGame;

/**
 * some functions may be useful when debugging
 * For example print the board state or print the tiles state
 *
 */
public class debugAssistant {
    public static void main(String[] args) {
        FocusGame focusGame = new FocusGame();
        focusGame.addTileToBoard("f001");
        focusGame.printBoardStates();
        focusGame.printTilesStates();
    }
}
