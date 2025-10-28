package net.junebug.chrysalis.util.helpers;

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

    public static boolean hasNetherExpansion() {
        return isModLoaded("netherexp");
    }

    public static boolean hasHominid() {
        return isModLoaded("hominid");
    }

    public static boolean hasMausoleum() {
        return isModLoaded("mausoleum");
    }

    public static boolean hasRubinatedNether() {
        return isModLoaded("rubinated_nether");
    }

    public static boolean hasAlexsMobs() {
        return isModLoaded("alexsmobs");
    }

    public static boolean hasAlexsCaves() {
        return isModLoaded("alexscaves");
    }
}