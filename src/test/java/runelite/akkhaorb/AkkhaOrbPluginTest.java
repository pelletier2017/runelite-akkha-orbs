package runelite.akkhaorb;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AkkhaOrbPluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(AkkhaOrbPlugin.class);
        RuneLite.main(args);
    }
}