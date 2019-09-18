package comp1110.ass2;

public class Tile {

    private TileType tileType;
    private Location location;
    private Orientation orientation;

    public Tile(String placement) {
        this.tileType = null; // enum value of tileType : a, b, c, d, e, f, g, h, i, j
        this.location = null; // top-left point's location
        this.orientation = null; // enum value of tileType: N(0), E(1), S(2), W(3)
    }

    public Location getLocation() { return location; }
    public TileType getTileType() { return tileType; }
    public Orientation getOrientation() { return orientation; }

    public static Orientation placementToOrientation(String placement) {
        //
        return null;
    }

    public static Location placementToLocation(String placement) {
        //
        return null;
    }


    

}
