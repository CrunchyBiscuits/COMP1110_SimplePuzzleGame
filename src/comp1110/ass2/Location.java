package comp1110.ass2;


/**
 *  @param col: the first number of placement
 *  @param row: the second number of placement
 *
 *  getCol: get the number of the column
 *  getRow: get the number of the row
 */
public class Location {
    private int row;
    private int col;
    static final int LOC = -1;

    public Location(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public Location() {
        this.col = LOC;
        this.row = LOC;
    }


    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

}
