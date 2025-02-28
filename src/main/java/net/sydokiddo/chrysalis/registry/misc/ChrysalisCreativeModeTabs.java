package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;

public class ChrysalisCreativeModeTabs {

    /**
     * Registers the debug utility items in the creative mode inventory.
     **/

    private static final ResourceKey<CreativeModeTab> CHRYSALIS_CREATIVE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Chrysalis.id(Chrysalis.MOD_ID));

    public static void registerCreativeTabs() {

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CHRYSALIS_CREATIVE_TAB, FabricItemGroup.builder()
            .title(Component.translatable("mod.chrysalis"))
            .icon(() -> new ItemStack(ChrysalisDebugItems.ICON))
            .build()
        );

        ItemGroupEvents.modifyEntriesEvent(CHRYSALIS_CREATIVE_TAB).register(content -> {
            content.accept(ChrysalisDebugItems.HEAL);
            addDebugItems(content);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {
            if (!entries.shouldShowOpRestrictedItems()) return;
            entries.addAfter(Items.DEBUG_STICK, ChrysalisDebugItems.HEAL);
            addDebugItems(entries);
            entries.addAfter(Items.PAINTING, Items.ENDER_DRAGON_SPAWN_EGG, Items.WITHER_SPAWN_EGG);
        });
    }

    private static void addDebugItems(FabricItemGroupEntries itemGroupEntries) {
        itemGroupEntries.addAfter(ChrysalisDebugItems.HEAL, ChrysalisDebugItems.FILL_HUNGER, ChrysalisDebugItems.FILL_OXYGEN, ChrysalisDebugItems.GIVE_RESISTANCE,
        ChrysalisDebugItems.CLEAR_EFFECTS, ChrysalisDebugItems.TELEPORT_TO_SPAWNPOINT, ChrysalisDebugItems.TELEPORT_WAND, ChrysalisDebugItems.KILL_WAND, ChrysalisDebugItems.AGGRO_WAND,
        ChrysalisDebugItems.TAME_MOB, ChrysalisDebugItems.RIDE_MOB);
    }
}