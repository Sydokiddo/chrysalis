package net.sydokiddo.chrysalis.util.helpers;

import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public class CompatibilityHelper {

    public static boolean hasEndlessEncore() {
        return FabricLoader.getInstance().isModLoaded("endless_encore");
    }

    public static boolean hasMonsterMash() {
        return FabricLoader.getInstance().isModLoaded("monster_mash");
    }

    public static boolean hasMicrocosm() {
        return FabricLoader.getInstance().isModLoaded("microcosm");
    }

    public static boolean hasNetherExpansion() {
        return FabricLoader.getInstance().isModLoaded("netherexp");
    }

    public static boolean hasElementalis() {
        return FabricLoader.getInstance().isModLoaded("elementalis");
    }
}