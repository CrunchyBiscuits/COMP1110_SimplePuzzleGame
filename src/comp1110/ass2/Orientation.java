package comp1110.ass2;

/**
 * author: Jianwu Yao
 */

/**
 * difine the four orientation: N E S W
 */
public enum Orientation {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private final int value;
    public int getValue() {
        return value;
    }

    Orientation(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "The direction now is:" + getValue();
    }

}
