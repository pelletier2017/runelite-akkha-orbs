package runelite.akkhaorb;

//import jdk.internal.net.http.common.Pair;

import net.runelite.api.Constants;
import net.runelite.api.Point;
import org.apache.commons.lang3.tuple.Pair;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

@Slf4j
public class LinePathOverlay extends Overlay {

    private final Client client;
    private final AkkhaOrbConfig config;
    private final AkkhaOrbPlugin plugin;

    @Inject
    private LinePathOverlay(Client client, AkkhaOrbConfig config, AkkhaOrbPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
//        int dangerousTickCount = config.dangerousTicks();
//        int cautiousTickCount = config.cautiousTicks();
        Arena arena = plugin.arena();


        List<Pair<WorldPoint, WorldPoint>> npcPaths = arena.getNpcPathLines();
        for (Pair<WorldPoint, WorldPoint> linePair : npcPaths) {
            if (linePair.getLeft().equals(linePair.getRight())) {
                // ignore lines that are just a dot
                continue;
            }
            Color color = config.dangerousColor();
            drawLinesOnWorld(graphics, client, linePair, color);
        }

        return null;
    }

    public static void drawLinesOnWorld(Graphics2D graphics, Client client, Pair<WorldPoint, WorldPoint> line,
                                        Color color) {
//        LocalPoint lp = LocalPoint.fromWorld(client, first);
//        LocalPoint startLp = QuestPerspective.getInstanceLocalPoint(client, linePoints.get(i));
//        LocalPoint endLp = QuestPerspective.getInstanceLocalPoint(client, linePoints.get(i + 1));

        LocalPoint startLp = LocalPoint.fromWorld(client, line.getLeft());
        LocalPoint endLp = LocalPoint.fromWorld(client, line.getRight());
        if (startLp == null || endLp == null) {
            log.error("One of these is null: startLp=" + startLp + " endLp=" + endLp);
            return;
        }

        Line2D.Double newLine = getWorldLines(client, startLp, endLp);
        if (newLine != null) {
            OverlayUtil.renderPolygon(graphics, newLine, color);
        }
    }

    private static Line2D.Double getWorldLines(@Nonnull Client client, @Nonnull LocalPoint startLocation, LocalPoint endLocation) {
        final int plane = client.getPlane();

        final int startX = startLocation.getX();
        final int startY = startLocation.getY();
        final int endX = endLocation.getX();
        final int endY = endLocation.getY();

        final int sceneX = startLocation.getSceneX();
        final int sceneY = startLocation.getSceneY();

        if (sceneX < 0 || sceneY < 0 || sceneX >= Constants.SCENE_SIZE || sceneY >= Constants.SCENE_SIZE)
        {
            return null;
        }

        final int startHeight = Perspective.getTileHeight(client, startLocation, plane);
        final int endHeight = Perspective.getTileHeight(client, endLocation, plane);

        net.runelite.api.Point p1 = Perspective.localToCanvas(client, startX, startY, startHeight);
        Point p2 = Perspective.localToCanvas(client, endX, endY, endHeight);

        if (p1 == null || p2 == null)
        {
            return null;
        }

        return new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

//    LocalPoint lp = LocalPoint.fromWorld(client, first);
//    Polygon polygon = Perspective.getCanvasTilePoly(client, lp);
//    Rectangle2D rectangle2D = polygon.getBounds2D();
}
