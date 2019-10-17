package comp1110.ass2;

/**
 * author: Jianwu Yao
 * from line 8 to line 44
 */

public class Tile {

    private TileType tileType;
    private Location location;
    private Orientation orientation;

    public Tile(String placement) {
        this.tileType = TileType.valueOf(Character.toString(placement.charAt(0)-32)); // enum value of tileType : a, b, c, d, e, f, g, h, i, j
        this.location = placementToLocation(placement); // top-left point's location
        this.orientation = placementToOrientation(placement); // enum value of tileType: N(0), E(1), S(2), W(3)
    }

    public Location getLocation() { return location; }
    public TileType getTileType() { return tileType; }
    public Orientation getOrientation() { return orientation; }

    public static Orientation placementToOrientation(String placement) {
        int direction = placement.charAt(3) - '0';
        switch (direction) {
            case 0:
                return Orientation.NORTH;
            case 1:
                return Orientation.EAST;
            case 2:
                return Orientation.SOUTH;
            case 3:
                return Orientation.WEST;
        }
        return null;
    }

    public static Location placementToLocation(String placement) {
        int x = placement.charAt(1) - '0';
        int y = placement.charAt(2) - '0';
        return new Location(x, y);
    }
}
