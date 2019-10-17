package comp1110.ass2;

/**
 * author: Jianwu Yao
 * from line 8 to line 133
 */

import static comp1110.ass2.State.*;

public enum TileType {
    A(3, 2), B(4, 2), C(4, 2), D(3, 2), E(3, 2),
    F(3, 1), G(3, 2), H(3, 3), I(2, 2), J(4, 2);

    private int width;
    private int height;

    TileType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth(int orientation) {
        return orientation % 2 == 0 ? this.width : this.height;
    }

    public int getHeight(int orientation) {
        return orientation % 2 == 0 ? this.height : this.width;
    }

    public State getOnePointState(TileType tileType, Orientation orientation, int colOff, int rowOff) {
        State[] states = statemap[this.ordinal()];
        if(tileType == A || tileType == D || tileType == E || tileType == G) { // checked
            if(colOff < 0 || rowOff < 0 || colOff > 3 || rowOff > 3) return  null;
            switch (orientation) {
                case NORTH:
                    return (colOff == 3) ? null : states[(rowOff * 3) + colOff];
                case EAST:
                    return (rowOff == 3) ? null : states[((1 - colOff) * 3) + rowOff];
                case SOUTH:
                    return (colOff == 3) ? null : states[((1 - rowOff) * 3) + (2 - colOff)];
                case WEST:
                    return (rowOff == 3) ? null : states[(colOff*3) + (2 - rowOff)];
            }
        }
        if(tileType == B || tileType == C || tileType == J) { // checked
            if(colOff < 0 || rowOff < 0 || colOff > 4 || rowOff > 4) return  null;
            switch (orientation) {
                case NORTH:
                    return (colOff == 4) ? null : states[(rowOff * 4) + colOff];
                case EAST:
                    return (rowOff == 4) ? null : states[((1 - colOff) * 4) + rowOff];
                case SOUTH:
                    return (colOff == 4) ? null : states[((1 - rowOff) * 4) + (3 - colOff)];
                case WEST:
                    return (rowOff == 4) ? null : states[(colOff * 4) + (3 - rowOff)];
            }
        }
        if(tileType == F) { // checked
            if(colOff < 0 || rowOff < 0 || colOff > 3 || rowOff > 3) return  null;
            switch (orientation) {
                case NORTH:
                    return (colOff == 3) ? null : states[colOff];
                case EAST:
                    return (rowOff == 3) ? null : states[2 -  rowOff];
                case SOUTH:
                    return (colOff == 3) ? null : states[2 - colOff];
                case WEST:
                    return (rowOff == 3) ? null : states[2 - rowOff];
            }
        }
        if(tileType == H) { // checked
            if(colOff < 0 || rowOff < 0 || colOff > 3 || rowOff > 3) return  null;
            switch (orientation) {
                case NORTH:
                    return (colOff == 3) ? null : states[(rowOff * 3) + colOff];
                case EAST:
                    return (rowOff == 3) ? null : states[(2 - colOff) * 3 + rowOff];
                case SOUTH:
                    return (colOff == 3) ? null : states[(2 - rowOff) * 3 + (2 - colOff)];
                case WEST:
                    return (rowOff == 3) ? null : states[colOff*3 + 2-rowOff];
            }
        }
        if(tileType == I) { // checked
            if(colOff < 0 || rowOff < 0 || colOff > 2 || rowOff > 2) return  null;
            switch (orientation) {
                case NORTH:
                    return (colOff == 2) ? null : states[(rowOff * 2) + colOff];
                case EAST:
                    return (rowOff == 2) ? null : states[(1 - colOff) * 2 + rowOff];
                case SOUTH:
                    return (colOff == 2) ? null : states[(1 - rowOff) * 2 + (1 - colOff)];
                case WEST:
                    return (rowOff == 2) ? null : states[colOff*2 + 1-rowOff];
            }
        }
        return null;
    }

    private static State[][] statemap = {
                    {GREEN, WHITE, RED,
                    EMPTY, RED, EMPTY}, // the initial state of A, checked

                    {EMPTY, BLUE, GREEN, GREEN,
                    WHITE, WHITE, EMPTY, EMPTY}, // the initial state of B, checked

                    {EMPTY, EMPTY, GREEN, EMPTY,
                    RED, RED, WHITE, BLUE}, // the initial state of C, checked

                    {RED, RED, RED,
                    EMPTY, EMPTY, BLUE}, // the initial state of D, checked

                    {BLUE, BLUE, BLUE,
                    RED, RED, EMPTY,}, // the initial state of E, checked

                    {WHITE, WHITE, WHITE}, // the initial state of F, checked

                    {WHITE, BLUE, EMPTY,
                    EMPTY, BLUE, WHITE}, // the initial state of G, checked

                    {RED, GREEN, GREEN,
                    WHITE, EMPTY, EMPTY,
                    WHITE, EMPTY, EMPTY}, // the initial state of H, checked

                    {BLUE, BLUE,
                     EMPTY, WHITE}, // the initial state of I, checked

                    {GREEN, GREEN, WHITE, RED,
                    GREEN, EMPTY, EMPTY, EMPTY}  // the initial state of J, checked
    };


}
