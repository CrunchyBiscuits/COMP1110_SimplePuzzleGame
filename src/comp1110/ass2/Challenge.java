package comp1110.ass2;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static comp1110.ass2.Objective.SOLUTIONS;

/**
 * define the states of the central nine pieces like 'RRRRRRRRR'
 * based on the game requirement such as when the player achieve the consistant three pieces present the same color,
 * then he/she win.
 *
 * For example: RRRGBWWBG each line no matter this is a vertical line or horientional line has the same color
 * which means the player wins
 */
public class Challenge {

    public static String randomChallenge() {
        String challenge = "";
        Random random = new Random();
        List<String> strings = Arrays.asList("R", "G", "B", "W");
        for (int i=0; i<9; i++) {
            int rand = random.nextInt(4);
            challenge += strings.get(rand);
        }
        return challenge;
    }

    public static String getInterestingChallenge() {
        Random random = new Random();
        int randInt = random.nextInt(SOLUTIONS.length);
        return SOLUTIONS[randInt].objective;
    }
}


