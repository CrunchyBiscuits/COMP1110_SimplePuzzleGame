package comp1110.ass2;

import java.util.Arrays;
import java.util.List;
import java.util.Random;



/**
 * define the states of the central nine pieces like 'RRRRRRRRR'
 * based on the game requirement such as when the player achieve the consistant three pieces present the same color,
 * then he/she win.
 *
 * For example: RRRGBWWBG each line no matter this is a vertical line or horientional line has the same color
 * which means the player wins
 *
 *
 * author: Siyu Zhou
 */
public class Challenge {

    public static String getInterestingChallenge() {
        Random random = new Random();
        int randInt = random.nextInt(Solution.SOLUTIONS.length);
        return Solution.SOLUTIONS[randInt].objective;
    }
}


