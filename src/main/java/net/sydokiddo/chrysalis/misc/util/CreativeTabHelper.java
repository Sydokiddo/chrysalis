package net.sydokiddo.chrysalis.misc.util;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import java.util.ArrayList;
import java.util.List;

public class CreativeTabHelper {

    /**
     * Assists with adding Instrument items and their NBT variants to any Creative Mode tab
     **/

    @SuppressWarnings("unused")
    public static void addInstrumentItem(Item comparedItem, Item instrument, TagKey<Instrument> tagKey, CreativeModeTab.TabVisibility tabVisibility, ResourceKey<CreativeModeTab> creativeModeTabs) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs).register((entries) -> {

            List<ItemStack> list = new ArrayList<>();

            for (Holder<Instrument> holder : BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey)) {
                var itemStack = InstrumentItem.create(instrument, holder);
                itemStack.setCount(1);
                list.add(itemStack);
            }
            entries.addBefore(comparedItem, list, tabVisibility);
        });
    }

    public static void init() {}
}