package comp1110.ass2;

import org.junit.Test;

import static comp1110.ass2.State.*;
import static comp1110.ass2.State.GREEN;
import static junit.framework.TestCase.assertTrue;

public class CharToStateTest {
    @Test
    public void testCharToState() {
        FocusGame focusGame = new FocusGame();
        assertTrue("test input 'R' is successful", focusGame.charToState('R') == RED);
        assertTrue("test input 'B' is successful", focusGame.charToState('B') == BLUE);
        assertTrue("test input 'W' is successful", focusGame.charToState('W') == WHITE);
        assertTrue("test input 'G' is successful", focusGame.charToState('G') == GREEN);
    }

}
