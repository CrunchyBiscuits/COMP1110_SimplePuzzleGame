package comp1110.ass2;

import static comp1110.ass2.TileType.*;

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

    /*public static TileType placementToTiletype(String placement) {
        char tileType = placement.charAt(0);
        switch (tileType) {
            case 'a':
                return A;
            case 'b':
                return B;
            case 'c':
                return C;
            case 'd':
                return D;
            case 'e':
                return E;
            case 'f':
                return F;
            case 'g':
                return G;
            case 'h':
                return H;
            case 'i':
                return I;
            case 'j':
                return J;
        }
        return null;
    }*/

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
        int locationCol = placement.charAt(1) - '0';
        int locationRow = placement.charAt(2) - '0';
        return new Location(locationCol, locationRow);
    }



}
