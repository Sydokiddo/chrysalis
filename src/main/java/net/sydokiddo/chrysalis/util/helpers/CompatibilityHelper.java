package net.sydokiddo.chrysalis.util.helpers;

import net.neoforged.fml.loading.FMLLoader;

@SuppressWarnings("unused")
public class CompatibilityHelper {

    public static boolean isModLoaded(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    public static boolean hasEndlessEncore() {
        return isModLoaded("endless_encore");
    }

    public static boolean hasMonsterMash() {
        return isModLoaded("monster_mash");
    }

    public static boolean hasTheBeginningAndTheEnd() {
        return isModLoaded("the_beginning_and_the_end");
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