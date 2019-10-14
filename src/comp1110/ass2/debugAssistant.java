package comp1110.ass2;

import java.util.ArrayList;


/**
 * author: Jianwu Yao
 * some functions may be useful when debugging
 * For example print the board state or print the tiles state
 * may this loooks stupid, but it was way helpful when I was working on task5-6-9
 * thanks to debugAssistant
 */
public class debugAssistant {
    public static void main(String[] args) {
        FocusGame focusGame = new FocusGame();
        //String challenge = "RRRRRRRRR";

        ArrayList<String> challenges = new ArrayList<>();
        challenges.add("RRRRRRRRR");
        challenges.add("RRRBWBBRB");
        challenges.add("WWWWWWWWW");
        challenges.add("GGRBGRBBR");
        challenges.add("WBWWBWGGG");

        //expert RRRRRRRRR RRRRRWRWW BBBBWBBBB
        //result [a300, b532, c122, d513, e232, f000, g611, h601, i030, j010]
        // e232 c122 j010 a300 d513
        // b532 f000 g611 h601 i030

        //e232c122j010a300d513f000h601g613i030b532

        //BBBBWBBBB
        //[a021, b102, c430, d223, e402, f711, g321, h000, i523, j701]
        //[a500, b103, c703, d200, e513, f140, g210, h522, i330, j003]


        String challenge = "BBBBWBBBB";


        System.out.println(FocusGame.getAllNineCentralPointsSolution(challenge));
//        String solution = FocusGame.getSolution(challenge);
//        System.out.println(solution);


        //starter RRRBWBBRB
        //result [a000, b013, c113, d302, e323, f400, g420, h522, i613, j701]

        //wizard WWWWWWWWW
        //result [a022, b132, c430, d003, e611, f511, g110, h400, i200, j701]

        //master GGRBGRBBR
        //result [a613, b010, c703, d411, e221, f500, g030, h201, i003, j432]

        //junior WBWWBWGGG
        //result [a723, b130, c330, d613, e100, f120, g310, h601, i400, j003]


        //Set<String> possibleSolutions = new HashSet<>();

        //focusGame.challengeBoardStates(focusGame, challenge);

        //possibleSolutions = focusGame.getViablePiecePlacements(null, challenge, 3, 1);
        //System.out.println(possibleSolutions);


        //System.out.println(focusGame.getAllNineCentralPointsSolution(challenge));

//        Set<String> justfortest = focusGame.getAllNineCentralPointsSolution("WBWWBWGGG");
        //System.out.println(justfortest.size());
//        for(String test : challenges) {
//            System.out.println(focusGame.getAllNineCentralPointsSolution(test));
//        }

//        Set<String> garbageSet = new HashSet<>();
//        for(String test: justfortest) {
//            if(!focusGame.checkValidBoardState(test)) {
//                garbageSet.add(test);
//            }
//        }

//        justfortest.removeAll(garbageSet);
//        //System.out.println(justfortest);
//        System.out.println(justfortest.size());

//        Set<String> testSet = new HashSet<>();
//        System.out.println(testSet.size() == 0);

//        Set<String> test1 = new HashSet<>();
//        test1.add("qqq");
//        test1.add("ddd");
//        Set<Set<String>> a = new HashSet<>();
//        for(String str: test1) {
//            Set<String> test2 = new HashSet<>();
//            test2.add(str+"1");
//            a.add(test2);
//        }
//        System.out.println(a);
    }
}
