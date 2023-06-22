package net.sydokiddo.chrysalis.registry;

import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CreativeTabHelper;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.items.DebugItems;
import net.sydokiddo.chrysalis.registry.misc.ModCreativeModeTabs;
import net.sydokiddo.chrysalis.registry.misc.ModDamageSources;
import net.sydokiddo.chrysalis.registry.misc.ModResourcePacks;

public class ModRegistry {

    public static void registerAll() {

        ModResourcePacks.registerResourcePacks();
        RegistryHelpers.init();
        CreativeTabHelper.init();
        ModDamageSources.registerDamageSources();

        if (Chrysalis.IS_DEBUG) {
            registerDebugUtilities();
        }
    }

    private static void registerDebugUtilities() {
        DebugItems.registerDebugItems();
        ModCreativeModeTabs.registerCreativeTabs();
        Chrysalis.LOGGER.info("Debug environment detected! Initializing debug utilities for Chrysalis.");
    }
}
