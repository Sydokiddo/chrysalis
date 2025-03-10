package net.sydokiddo.chrysalis.util.helpers;

import net.neoforged.fml.loading.FMLLoader;

@SuppressWarnings("unused")
public class CompatibilityHelper {

    public static boolean isModLoaded(String modID) {
        return FMLLoader.getLoadingModList().getModFileById(modID) != null;
    }

    public static boolean hasEndlessEncore() {
        return isModLoaded("endless_encore");
    }

    public static boolean hasMonsterMash() {
        return isModLoaded("monster_mash");
    }

    public static boolean hasMicrocosm() {
        return isModLoaded("microcosm");
    }

    public static boolean hasNetherExpansion() {
        return isModLoaded("netherexp");
    }

    public static boolean hasElementalis() {
        return isModLoaded("elementalis");
    }

    public static boolean hasHominid() {
        return isModLoaded("hominid");
    }
}