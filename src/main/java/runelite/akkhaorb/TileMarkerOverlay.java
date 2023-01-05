package runelite.akkhaorb;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.*;
import runelite.akkhaorb.AkkhaOrbConfig;
import runelite.akkhaorb.AkkhaOrbPlugin;
import runelite.akkhaorb.Arena;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class TileMarkerOverlay extends Overlay {
    private static final int MAX_DRAW_DISTANCE = 32;

    private final Client client;
    private final AkkhaOrbConfig config;
    private final AkkhaOrbPlugin plugin;

    @Inject
    private TileMarkerOverlay(Client client, AkkhaOrbConfig config, AkkhaOrbPlugin plugin) {
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

        if (config.shouldShowSafeTiles()) {
            List<WorldPoint> safeTiles = arena.getSafeTiles();
            drawTiles(graphics, safeTiles, config.safeColor());
        }
        if (config.shouldShowCautiousTiles()) {
            List<WorldPoint> cautiousTiles = arena.getCautiousTiles();
            drawTiles(graphics, cautiousTiles, config.cautiousColor());
        }
        if (config.shouldShowDangerousTiles()) {
            List<WorldPoint> dangerousTiles = arena.getDangerousTiles();
            drawTiles(graphics, dangerousTiles, config.dangerousColor());
        }

        return null;
    }

    private void drawTiles(Graphics2D graphics, List<WorldPoint> tiles, Color color) {
        for (WorldPoint worldPoint : tiles) {
            if (worldPoint.getPlane() != client.getPlane()) {
                continue;
            }
            drawTile(graphics, worldPoint, color);
        }
    }

    private void drawTile(Graphics2D graphics, WorldPoint point, Color color) {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

        if (point.distanceTo(playerLocation) >= MAX_DRAW_DISTANCE) {
            return;
        }

        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null) {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly != null) {
            Stroke borderStroke = new BasicStroke((float) config.borderWidth());
        // TODO make Opacity a config and create a new fill color using hte opacity? Same color for border + fill?
//            OverlayUtil.renderPolygon(graphics, poly, borderColor, fillColor, borderStroke);
            int alpha = (int)(0.2 * 255);
            Color newFillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            OverlayUtil.renderPolygon(graphics, poly, color, newFillColor, borderStroke);
        }
    }
}
