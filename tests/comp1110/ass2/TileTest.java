package comp1110.ass2;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;

import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TileTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private boolean isEmpty = true;

    @Test
    public void testType(){
        if (!isEmpty){
            fail();
        }else {
            Tile aTile=new Tile("a000");
            assertTrue("input was a000 expected "+TileType.A.name()+" but got "+aTile.getTileType().name(),aTile.getTileType()==TileType.A);
            Tile a1Tile=new Tile("a120");
            assertTrue("input was a000 expected "+TileType.A.name()+" but got "+a1Tile.getTileType().name(),a1Tile.getTileType()==TileType.A);
            Tile bTile=new Tile("b000");
            assertTrue("input was b000 expected "+TileType.B.name()+" but got "+bTile.getTileType().name(),bTile.getTileType()==TileType.B);
            Tile cTile=new Tile("c000");
            assertTrue("input was c000 expected "+TileType.C.name()+" but got "+cTile.getTileType().name(),cTile.getTileType()==TileType.C);
            Tile dTile=new Tile("d000");
            assertTrue("input was d000 expected "+TileType.D.name()+" but got "+dTile.getTileType().name(),dTile.getTileType()==TileType.D);
            Tile eTile=new Tile("e000");
            assertTrue("input was e000 expected "+TileType.E.name()+" but got "+eTile.getTileType().name(),eTile.getTileType()==TileType.E);
            Tile fTile=new Tile("f000");
            assertTrue("input was f000 expected "+TileType.F.name()+" but got "+fTile.getTileType().name(),fTile.getTileType()==TileType.F);
            Tile gTile=new Tile("g000");
            assertTrue("input was g000 expected "+TileType.G.name()+" but got "+gTile.getTileType().name(),gTile.getTileType()==TileType.G);
            Tile hTile=new Tile("h000");
            assertTrue("input was h000 expected "+TileType.H.name()+" but got "+hTile.getTileType().name(),hTile.getTileType()==TileType.H);
            Tile iTile=new Tile("i000");
            assertTrue("input was i000 expected "+TileType.I.name()+" but got "+iTile.getTileType().name(),iTile.getTileType()==TileType.I);
            Tile jTile=new Tile("j000");
            assertTrue("input was j000 expected "+TileType.J.name()+" but got "+jTile.getTileType().name(),jTile.getTileType()==TileType.J);
        }
    }

    @Test
    public void testOrientation(){
        if (!isEmpty){
            fail();
        }else {
            Tile nTile = new Tile("a000");
            assertTrue("input was a000 expected "+Orientation.NORTH.name()+" but got "+nTile.getOrientation().name(),nTile.getOrientation()==Orientation.NORTH);
            Tile eTile = new Tile("a001");
            assertTrue("input was a001 expected "+Orientation.EAST.name()+" but got "+eTile.getOrientation().name(),eTile.getOrientation()==Orientation.EAST);
            Tile sTile = new Tile("a002");
            assertTrue("input was a002 expected "+Orientation.SOUTH.name()+" but got "+sTile.getOrientation().name(),sTile.getOrientation()==Orientation.SOUTH);
            Tile wTile = new Tile("a003");
            assertTrue("input was a003 expected "+Orientation.WEST.name()+" but got "+wTile.getOrientation().name(),wTile.getOrientation()==Orientation.WEST);

        }
    }

    @Test
    public void testPlacement2Location(){
        if (!isEmpty){
            fail();
        }else {
            Tile start=new Tile("a000");
            assertTrue("input was a000 expected x:0 y:0 but got x:"+start.getLocation().getCol()+" y:"+start.getLocation().getRow(),start.getLocation().getCol()==0&&start.getLocation().getRow()==0);
            Random random=new Random();
            int x = random.nextInt(10);
            int y = random.nextInt(5);
            Tile randomTile=new Tile("a"+x+y+"0");
            assertTrue("input was a"+x+y+"0 expected x:"+x+" y:"+y+" but got x:"+randomTile.getLocation().getCol()+" y:"+randomTile.getLocation().getRow(),randomTile.getLocation().getCol()==x&&randomTile.getLocation().getRow()==y);
        }
    }
}
