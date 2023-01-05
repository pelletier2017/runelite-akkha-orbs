package runelite.akkhaorb;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
        name = "Akkha Orbs"
)
public class AkkhaOrbPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private TileMarkerOverlay tileMarkerOverlay;

    @Inject
    private LinePathOverlay linePathOverlay;

    @Inject
    private AkkhaOrbConfig config;

    @Inject
    private Arena arena;

    @Override
    protected void startUp() throws Exception {
        log.info("Startup");
        updateOverlay();
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Example stopped!");
        removeOverlays();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        log.info("Config changed");
        updateOverlay();
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        log.info("Chat Message");
        arena.onChatMessage(event);
    }

    private void updateOverlay() {
        removeOverlays();

        if (config.shouldShowTileOverlay()) {
            overlayManager.add(tileMarkerOverlay);
        }

        if (config.shouldShowLineOverlay()) {
            overlayManager.add(linePathOverlay);
        }
    }

    private void removeOverlays() {
        overlayManager.remove(tileMarkerOverlay);
        overlayManager.remove(linePathOverlay);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        arena.onGameTick();
    }

    public Arena arena() {
        return arena;
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned npcSpawned) {
//        log.info("npc spawned");
//        log.info("npcName=" + npcSpawned.getNpc().getName());
//        log.info("npcIndex=" + npcSpawned.getNpc().getIndex());
//        log.info("npcId=" + npcSpawned.getNpc().getId());
        arena.addNpc(npcSpawned.getNpc());
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned npcDespawned) {
        arena.removeNpc(npcDespawned.getNpc());
    }

    @Provides
    AkkhaOrbConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AkkhaOrbConfig.class);
    }
}
