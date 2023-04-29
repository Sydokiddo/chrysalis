package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.sydokiddo.chrysalis.registry.items.DebugItems;

public class ModCreativeModeTabs {

    @SuppressWarnings("ALL")
    public static void registerCreativeTabs() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries ->
        entries.addAfter(Items.DEBUG_STICK, DebugItems.HEAL, DebugItems.FILL_HUNGER, DebugItems.TELEPORT_TO_SPAWNPOINT, DebugItems.KILL_WAND));
    }
}
