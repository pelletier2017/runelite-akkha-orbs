package runelite.akkhaorb;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("akkhaorb")
public interface AkkhaOrbConfig extends Config {

    @ConfigItem(
            keyName = "showDangerousTiles",
            name = "Show Dangerous Tiles",
            description = "Whether to show dangerous tiles or not"
    )
    default boolean shouldShowDangerousTiles() {
        return false;
    }

    @Alpha
    @ConfigItem(
            keyName = "dangerousColor",
            name = "Color Border Dangerous",
            description = "Configures the color of dangerous tiles"
    )
    default Color dangerousColor() {
        return Color.RED;
    }

//    @Alpha
//    @ConfigItem(
//            keyName = "dangerousFillColor",
//            name = "Color Fill Dangerous",
//            description = "Configures the color of dangerous tiles"
//    )
//    default Color dangerousFillColor() {
//        return new Color(255, 0, 0, 0.3f);
//    }

    @ConfigItem(
            keyName = "dangerousTicks",
            name = "Tick Count Dangerous",
            description = "Number of safe ticks before tile is marked dangerous"
    )
    default int dangerousTicks() {
        return 2;
    }

    @ConfigItem(
            keyName = "showCautiousTiles",
            name = "Show Cautious Tiles",
            description = "Whether to show cautious tiles or not"
    )
    default boolean shouldShowCautiousTiles() {
        return false;
    }

    @Alpha
    @ConfigItem(
            keyName = "cautiousColor",
            name = "Color Border Cautious",
            description = "Configures the color of cautious tiles"
    )
    default Color cautiousColor() {
        return Color.YELLOW;
    }

//    @Alpha
//    @ConfigItem(
//            keyName = "cautiousFillColor",
//            name = "Color Fill Cautious",
//            description = "Configures the color of cautious tiles"
//    )
//    default Color cautiousFillColor() {
//        return new Color(255, 255, 0, 0.3f);
//    }

    @ConfigItem(
            keyName = "cautiousTicks",
            name = "Tick Count cautious",
            description = "Number of safe ticks before tile is marked cautious. (Must be more than dangerous)"
    )
    default int cautiousTicks() {
        return 7;
    }

    @ConfigItem(
            keyName = "showSafeTiles",
            name = "Show Safe Tiles",
            description = "Whether to show safe tiles or not"
    )
    default boolean shouldShowSafeTiles() {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "safeColor",
            name = "Color Border Safe",
            description = "Configures the color of safe tiles"
    )
    default Color safeColor() {
        return Color.GREEN;
    }

//    @Alpha
//    @ConfigItem(
//            keyName = "safeFillColor",
//            name = "Color Fill Safe",
//            description = "Configures the color of safe tiles"
//    )
//    default Color safeFillColor() {
//        return new Color(0, 255, 0, 0.3f);
//    }

    @ConfigItem(
            keyName = "borderWidth",
            name = "Border Width",
            description = "Width of the marked tile border"
    )
    default double borderWidth() {
        return 0.0;
    }

    @ConfigItem(
            keyName = "showTileOverlay",
            name = "Overlay Show Tiles",
            description = "Whether to show tile overlay or not"
    )
    default boolean shouldShowTileOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "showLineOverlay",
            name = "Overlay Show Lines",
            description = "Whether to show line overlay or not"
    )
    default boolean shouldShowLineOverlay() {
        return true;
    }

    @ConfigItem(
            keyName = "shouldLimitOverlay",
            name = "Limit overlay to arena",
            description = "Whether to show line overlay or not"
    )
    default boolean shouldLimitOverlay() {
        return true;
    }

}
