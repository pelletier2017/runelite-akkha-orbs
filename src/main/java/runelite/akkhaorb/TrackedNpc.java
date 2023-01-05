package runelite.akkhaorb;

import lombok.ToString;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.List;

@ToString
public class TrackedNpc {

    private NPC npc;

    private WorldPoint currentLocation;

    private Direction direction;

    private String name;

    public TrackedNpc(NPC npc) {
        this.npc = npc;
        this.name = npc.getName();
        currentLocation = npc.getWorldLocation();
    }

    public WorldPoint getRealLocation() {
        return npc.getWorldLocation();
    }

    public void updateTrackedLocation() {
        WorldPoint newLocation = npc.getWorldLocation();
        direction = Direction.fromAtoB(currentLocation, newLocation);
        currentLocation = newLocation;
    }

    public Direction getDirection() {
        return this.direction;
    }

    // assumes movement is 1 tick for now
    public List<WorldPoint> predictFutureLocation(int startTime, int endTime) {
        List<WorldPoint> positions = new ArrayList<>();
        for (int i = startTime; i <= endTime; i++) {
            int newX = getRealLocation().getX() + (i * direction.getDx());
            int newY = getRealLocation().getY() + (i * direction.getDy());
            positions.add(new WorldPoint(newX, newY, getRealLocation().getPlane()));
        }
        return positions;
    }
}
