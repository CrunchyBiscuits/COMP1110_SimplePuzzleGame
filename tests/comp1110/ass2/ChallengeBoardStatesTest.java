package comp1110.ass2;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ChallengeBoardStatesTest {
    @Test
    public void testChallengeBoardStates() {
        FocusGame focusGame = new FocusGame();
        focusGame.initializeBoardState("", false, 0, 0);
        String challenge1 = "RRRWWWBBB";
        focusGame.challengeBoardStates(focusGame, challenge1);
        assertTrue("the 1st is RED", focusGame.printPointState(3, 1) == State.RED);
        assertTrue("the 2nd is RED", focusGame.printPointState(4, 1) == State.RED);
        assertTrue("the 3rd is RED", focusGame.printPointState(5, 1) == State.RED);
        assertTrue("the 4th is WHITE", focusGame.printPointState(3, 2) == State.WHITE);
        assertTrue("the 5th is WHITE", focusGame.printPointState(4, 2) == State.WHITE);
        assertTrue("the 6th is WHITE", focusGame.printPointState(5, 2) == State.WHITE);
        assertTrue("the 7th is BLUE", focusGame.printPointState(3, 3) == State.BLUE);
        assertTrue("the 8th is BLUE", focusGame.printPointState(4, 3) == State.BLUE);
        assertTrue("the 9th is BLUE", focusGame.printPointState(5, 3) == State.BLUE);
    }
}
