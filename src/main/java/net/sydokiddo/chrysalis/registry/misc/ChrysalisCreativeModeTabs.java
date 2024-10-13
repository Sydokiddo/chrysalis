package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;

public class ChrysalisCreativeModeTabs {

    /**
     * Registers the debug items in the Creative Mode inventory.
     **/

    public static void registerCreativeTabs() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {

        if (!entries.shouldShowOpRestrictedItems()) return;

        entries.addAfter(Items.DEBUG_STICK, ChrysalisDebugItems.HEAL, ChrysalisDebugItems.FILL_HUNGER, ChrysalisDebugItems.GIVE_RESISTANCE,
        ChrysalisDebugItems.CLEAR_EFFECTS, ChrysalisDebugItems.TELEPORT_TO_SPAWNPOINT, ChrysalisDebugItems.TELEPORT_WAND, ChrysalisDebugItems.KILL_WAND,
        ChrysalisDebugItems.TAME_MOB, ChrysalisDebugItems.RIDE_MOB);

        entries.addAfter(Items.PAINTING, Items.ENDER_DRAGON_SPAWN_EGG, Items.WITHER_SPAWN_EGG);
        });
    }
}