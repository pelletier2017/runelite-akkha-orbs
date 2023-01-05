package runelite.akkhaorb;

import com.google.common.collect.ImmutableList;
//import jdk.internal.net.http.common.Pair;
import net.runelite.api.events.ChatMessage;
import org.apache.commons.lang3.tuple.Pair;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ToString
@Slf4j
public class Arena {
    // 11804 = Unstable orb
    /// 11795 = akkha real
    // 11798 = fake akkah

    private AkkhaOrbConfig config;

    private static final List<Integer> NPC_TO_TRACK = Arrays.asList(
            NpcID.UNSTABLE_ORB,
            NpcID.GOBLIN,
            NpcID.GOBLIN_656,
            3035,
            3034,
            3033,
            3032,
            3031,
            3029,
            3017
    );

    private static final String ORB_START_MESSAGE = "Magical orbs begin";
    private static final String ORB_END_MESSAGE = "Challenge complette: Akkha.";

    private Map<Integer, TrackedNpc> trackedNpcs = new HashMap<>();
    private List<WorldPoint> arenaTiles;
    private boolean isOrbPhase;

    @Inject
    public Arena(AkkhaOrbConfig config) {
        this.config = config;
//        arenaTiles = akkaArenaTiles();
        arenaTiles = goblinArenaTiles();
    }

    // TODO remove, just for testing
    private List<WorldPoint> goblinArenaTiles() {
        int xStart = 3246;
        int xEnd = 3265;
        int yStart = 3219;
        int yEnd = 3242;
        List<WorldPoint> tiles = new ArrayList<>();
        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                int goblinPlane = 0;
                tiles.add(new WorldPoint(x, y, goblinPlane));
            }
        }
        return tiles;
    }

    // TODO store points as LocalPoint instead of WorldPoint?
    private List<WorldPoint> akkaArenaTiles() {
        // irregular circle shape grabbed manually using dev tools
        List<WorldPoint> tiles = new ArrayList<>();

        tiles.addAll(tilesInRange(10711, 5086, 5089));
        tiles.addAll(tilesInRange(10712, 5083, 5092));
        tiles.addAll(tilesInRange(10713, 5082, 5093));
        tiles.addAll(tilesInRange(10714, 5081, 5094));
        tiles.addAll(tilesInRange(10715, 5080, 5095));
        tiles.addAll(tilesInRange(10716, 5079, 5096));
        tiles.addAll(tilesInRange(10717, 5079, 5096));
        tiles.addAll(tilesInRange(10718, 5079, 5096));
        tiles.addAll(tilesInRange(10719, 5078, 5097));
        tiles.addAll(tilesInRange(10720, 5078, 5097));
        tiles.addAll(tilesInRange(10721, 5078, 5097));
        tiles.addAll(tilesInRange(10722, 5078, 5097));
        tiles.addAll(tilesInRange(10723, 5079, 5096));
        tiles.addAll(tilesInRange(10724, 5079, 5096));
        tiles.addAll(tilesInRange(10725, 5079, 5096));
        tiles.addAll(tilesInRange(10726, 5080, 5095));
        tiles.addAll(tilesInRange(10727, 5081, 5094));
        tiles.addAll(tilesInRange(10728, 5082, 5093));
        tiles.addAll(tilesInRange(10729, 5083, 5092));
        tiles.addAll(tilesInRange(10730, 5086, 5089));
        return tiles;
    }

    private List<WorldPoint> tilesInRange(int x, int yStart, int yEnd) {
        // 13783, 3358 -> 3361 (far left column)
        // TODO does akkha world move??
        int xOffset = 13783 - 10711;
        int yOffset = 3358 - 5086;

        List<WorldPoint> points = new ArrayList<>();
        for (int y = yStart; y <= yEnd; y++) {
            int akkhaArenaPlane = 1;
            points.add(new WorldPoint(x + xOffset, y + yOffset, akkhaArenaPlane));
        }
        return points;
    }

    public void addNpc(NPC npc) {
        if (NPC_TO_TRACK.contains(npc.getId())) {
            trackedNpcs.put(npc.getIndex(), new TrackedNpc(npc));
        }
    }

    public void removeNpc(NPC npc) {
        if (NPC_TO_TRACK.contains(npc.getId())) {
            trackedNpcs.remove(npc.getIndex());
        }
    }

    public void onGameTick() {
        for (TrackedNpc trackedNpc : trackedNpcs.values()) {
            trackedNpc.updateTrackedLocation();
        }
    }

    public void onChatMessage(ChatMessage event) {
        // TODO do i need this at all because it only shows when unstable orbs are visible?
        String message = event.getMessage().toLowerCase();
        if (message.contains(ORB_START_MESSAGE)) {
            setOrbPhase(true);
        }
        if (message.contains(ORB_END_MESSAGE)) {
            setOrbPhase(false);
        }
    }

    private void setOrbPhase(boolean isOrbPhase) {
        this.isOrbPhase = isOrbPhase;
    }

    public List<WorldPoint> getArenaTiles() {
        return Collections.unmodifiableList(arenaTiles);
    }

    public List<Pair<WorldPoint, WorldPoint>> getNpcPathLines() {
        int cautiousTickCount = config.cautiousTicks();

        List<Pair<WorldPoint, WorldPoint>> linePairs = new ArrayList<>();
        for (TrackedNpc trackedNpc : trackedNpcs.values()) {
            List<WorldPoint> futureLocations = new ArrayList<>(trackedNpc.predictFutureLocation(0, cautiousTickCount));
            if (config.shouldLimitOverlay()) {
                futureLocations.removeIf(worldPoint -> !arenaTiles.contains(worldPoint));
            }

            if (futureLocations.size() >= 2) {
                WorldPoint start = futureLocations.get(0);
                WorldPoint end = futureLocations.get(futureLocations.size() - 1);
                linePairs.add(Pair.of(start, end));
            }
        }
        return Collections.unmodifiableList(linePairs);
    }

    public List<WorldPoint> getDangerousTiles() {
        int dangerousTickCount = config.dangerousTicks();
        return Collections.unmodifiableList(futureLocationsOfNpcs(0, dangerousTickCount));
    }

    public List<WorldPoint> getCautiousTiles() {
        int dangerousTickCount = config.dangerousTicks();
        int cautiousTickCount = config.cautiousTicks();
        return Collections.unmodifiableList(futureLocationsOfNpcs(dangerousTickCount + 1, cautiousTickCount));
    }

    public List<WorldPoint> getSafeTiles() {
        List<WorldPoint> safeTiles = new ArrayList<>(arenaTiles);
        safeTiles.removeAll(getDangerousTiles());
        safeTiles.removeAll(getCautiousTiles());
        return Collections.unmodifiableList(safeTiles);
    }

    public List<WorldPoint> futureLocationsOfNpcs(int startTick, int endTick) {
        List<WorldPoint> futureLocations = new ArrayList<>();
        for (TrackedNpc trackedNpc : trackedNpcs.values()) {
            futureLocations.addAll(trackedNpc.predictFutureLocation(startTick, endTick));
        }
        if (config.shouldLimitOverlay()) {
            futureLocations.removeIf(worldPoint -> !arenaTiles.contains(worldPoint));
        }

        return futureLocations.stream().distinct().collect(Collectors.toList());
    }


}
