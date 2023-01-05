package runelite.akkhaorb;

import lombok.Getter;
import lombok.ToString;
import net.runelite.api.coords.WorldPoint;

@ToString
@Getter
public enum Direction {

    STATIONARY(0, 0),
    IRREGULAR(0, 0),

    NORTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0),
    SOUTH(0, -1),

    NORTH_WEST(-1, 1),
    SOUTH_WEST(-1, -1),
    NORTH_EAST(1, 1),
    SOUTH_EAST(1, -1),
        ;

    private int dx;
    private int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction fromAtoB(WorldPoint a, WorldPoint b) {
        if (sameX(a, b) && sameY(a, b)) {
            return STATIONARY;
        }

        int dx = b.getX() - a.getX();
        int dy = b.getY() - a.getY();

        // cardinal directions
        if (dx == 0) {
            if (dy > 0) {
                return NORTH;
            } else {
                return SOUTH;
            }
        } else if (dy == 0) {
            if (dx > 0) {
                return EAST;
            } else {
                return WEST;
            }
        }

        // remove everything that isnt diagonals
        if (Math.abs(dx) != Math.abs(dy)) {
            return IRREGULAR;
        }

        // handle diagonals
        if (dx > 0) {
            if (dy > 0) {
                return NORTH_EAST;
            } else {
                return SOUTH_EAST;
            }
        } else {
            if (dy > 0) {
                return NORTH_WEST;
            } else {
                return SOUTH_WEST;
            }
        }
    }

    private static boolean sameX(WorldPoint a, WorldPoint b) {
        return a.getX() == b.getX();
    }

    private static boolean sameY(WorldPoint a, WorldPoint b) {
        return a.getY() == b.getY();
    }
}
