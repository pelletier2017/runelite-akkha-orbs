package runelite.akkhaorb;

import net.runelite.api.coords.WorldPoint;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void testWest() {
        assertEquals(Direction.fromAtoB(point(0, 0), point(-1, 0)), Direction.WEST);
        assertEquals(Direction.fromAtoB(point(10, 10), point(9, 10)), Direction.WEST);
        assertEquals(Direction.fromAtoB(point(0, -10), point(-1, -10)), Direction.WEST);
        assertEquals(Direction.fromAtoB(point(0, -10), point(-20, -10)), Direction.WEST);
        assertEquals(Direction.fromAtoB(point(-5, -10), point(-8, -10)), Direction.WEST);
    }

    @Test
    public void testEast() {
        assertEquals(Direction.fromAtoB(point(-1, 0), point(0, 0)), Direction.EAST);
        assertEquals(Direction.fromAtoB(point(9, 10), point(10, 10)), Direction.EAST);
        assertEquals(Direction.fromAtoB(point(-1, -10), point(0, -10)), Direction.EAST);
        assertEquals(Direction.fromAtoB(point(-20, -10), point(0, -10)), Direction.EAST);
        assertEquals(Direction.fromAtoB(point(-8, -10), point(-5, -10)), Direction.EAST);
    }

    @Test
    public void testNorth() {
        assertEquals(Direction.fromAtoB(point(0, -1), point(0, 0)), Direction.NORTH);
        assertEquals(Direction.fromAtoB(point(10, 9), point(10, 10)), Direction.NORTH);
        assertEquals(Direction.fromAtoB(point(-10, -1), point(-10, 0)), Direction.NORTH);
        assertEquals(Direction.fromAtoB(point(-10, -20), point(-10, 0)), Direction.NORTH);
        assertEquals(Direction.fromAtoB(point(-10, -8), point(-10, -5)), Direction.NORTH);
    }

    @Test
    public void testSouth() {
        assertEquals(Direction.fromAtoB(point(0, 0), point(0, -1)), Direction.SOUTH);
        assertEquals(Direction.fromAtoB(point(10, 10), point(10, 9)), Direction.SOUTH);
        assertEquals(Direction.fromAtoB(point(-10, 0), point(-10, -1)), Direction.SOUTH);
        assertEquals(Direction.fromAtoB(point(-10, 0), point(-10, -20)), Direction.SOUTH);
        assertEquals(Direction.fromAtoB(point(-10, -5), point(-10, -8)), Direction.SOUTH);
    }

    @Test
    public void testStationary() {
        assertEquals(Direction.fromAtoB(point(0, 0), point(0, 0)), Direction.STATIONARY);
    }

    @Test
    public void testIrregular() {
        assertEquals(Direction.fromAtoB(point(0, 0), point(25, 24)), Direction.IRREGULAR);
        assertEquals(Direction.fromAtoB(point(0, 0), point(24, 25)), Direction.IRREGULAR);
        assertEquals(Direction.fromAtoB(point(100, 100), point(110, 114)), Direction.IRREGULAR);
        assertEquals(Direction.fromAtoB(point(-100, -45), point(20, 32)), Direction.IRREGULAR);
    }


    @Test
    public void testNorthWest() {
        assertEquals(Direction.fromAtoB(point(0, 0), point(-1, 1)), Direction.NORTH_WEST);
        assertEquals(Direction.fromAtoB(point(0, 0), point(-10, 10)), Direction.NORTH_WEST);
        assertEquals(Direction.fromAtoB(point(10, 10), point(9, 11)), Direction.NORTH_WEST);
        assertEquals(Direction.fromAtoB(point(-10, -10), point(-11, -9)), Direction.NORTH_WEST);
    }

    @Test
    public void testSouthEast() {
        assertEquals(Direction.fromAtoB(point(-1, 1), point(0, 0)), Direction.SOUTH_EAST);
        assertEquals(Direction.fromAtoB(point(-10, 10), point(0, 0)), Direction.SOUTH_EAST);
        assertEquals(Direction.fromAtoB(point(9, 11), point(10, 10)), Direction.SOUTH_EAST);
        assertEquals(Direction.fromAtoB(point(-11, -9), point(-10, -10)), Direction.SOUTH_EAST);
    }

    @Test
    public void testNorthEast() {
        assertEquals(Direction.fromAtoB(point(-1, 0), point(0, 1)), Direction.NORTH_EAST);
        assertEquals(Direction.fromAtoB(point(-10, 0), point(0, 10)), Direction.NORTH_EAST);
        assertEquals(Direction.fromAtoB(point(9, 10), point(10, 11)), Direction.NORTH_EAST);
        assertEquals(Direction.fromAtoB(point(-11, -10), point(-10, -9)), Direction.NORTH_EAST);
    }

    @Test
    public void testSouthWest() {
        assertEquals(Direction.fromAtoB(point(0, 1), point(-1, 0)), Direction.SOUTH_WEST);
        assertEquals(Direction.fromAtoB(point(0, 10), point(-10, 0)), Direction.SOUTH_WEST);
        assertEquals(Direction.fromAtoB(point(10, 11), point(9, 10)), Direction.SOUTH_WEST);
        assertEquals(Direction.fromAtoB(point(-10, -9), point(-11, -10)), Direction.SOUTH_WEST);
    }

    private WorldPoint point(int x, int y) {
        return new WorldPoint(x, y, 0);
    }
}