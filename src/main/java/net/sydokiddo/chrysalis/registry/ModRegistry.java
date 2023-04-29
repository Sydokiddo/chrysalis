package net.sydokiddo.chrysalis.registry;

import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CreativeTabHelper;
import net.sydokiddo.chrysalis.registry.items.DebugItems;
import net.sydokiddo.chrysalis.registry.misc.ModCreativeModeTabs;

public class ModRegistry {

    public static void registerAll() {
        if (Chrysalis.IS_DEBUG) {
            registerDebugUtilities();
        }
    }

    private static void registerDebugUtilities() {
        DebugItems.registerDebugItems();
        ModCreativeModeTabs.registerCreativeTabs();
        CreativeTabHelper.init();
        Chrysalis.LOGGER.info("Debug environment detected! Initializing debug utilities for Chrysalis.");
    }
}
