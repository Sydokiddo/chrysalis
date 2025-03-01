package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;

public class ChrysalisCreativeModeTabs {

    /**
     * Registers the debug utility items in the creative mode inventory.
     **/

    private static final ResourceKey<CreativeModeTab> CHRYSALIS_CREATIVE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ChrysalisMod.id(ChrysalisMod.MOD_ID));

    public static void registerCreativeTabs() {

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CHRYSALIS_CREATIVE_TAB, FabricItemGroup.builder()
            .title(Component.translatable("mod.chrysalis"))
            .icon(() -> new ItemStack(ChrysalisDebugItems.ICON))
            .build()
        );

        ItemGroupEvents.modifyEntriesEvent(CHRYSALIS_CREATIVE_TAB).register(content -> addDebugItems(content, null));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {
            if (!entries.shouldShowOpRestrictedItems()) return;
            addDebugItems(entries, Items.DEBUG_STICK);
            entries.addAfter(Items.PAINTING, Items.ENDER_DRAGON_SPAWN_EGG, Items.WITHER_SPAWN_EGG);
        });
    }

    private static void addDebugItems(FabricItemGroupEntries itemGroupEntries, Item addAfter) {

        if (addAfter != null) itemGroupEntries.addAfter(addAfter, ChrysalisDebugItems.HEAL);
        else itemGroupEntries.accept(ChrysalisDebugItems.HEAL);

        itemGroupEntries.addAfter(ChrysalisDebugItems.HEAL, ChrysalisDebugItems.FILL_HUNGER, ChrysalisDebugItems.FILL_OXYGEN, ChrysalisDebugItems.GIVE_RESISTANCE,
        ChrysalisDebugItems.CLEAR_EFFECTS, ChrysalisDebugItems.TELEPORT_TO_SPAWNPOINT, ChrysalisDebugItems.TELEPORT_WAND, ChrysalisDebugItems.KILL_WAND, ChrysalisDebugItems.AGGRO_WAND,
        ChrysalisDebugItems.TAME_MOB, ChrysalisDebugItems.RIDE_MOB);
    }
}