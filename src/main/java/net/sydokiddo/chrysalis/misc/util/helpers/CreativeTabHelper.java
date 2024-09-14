package net.sydokiddo.chrysalis.misc.util.helpers;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Fireworks;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class CreativeTabHelper {

    /**
     * Assists with adding Instrument items and their variants to any Creative Mode tab.
     **/

    public static void addInstrumentItem(Item comparedItem, Item instrument, TagKey<Instrument> tagKey, CreativeModeTab.TabVisibility tabVisibility, ResourceKey<CreativeModeTab> creativeModeTab) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register((entries) -> {

            List<ItemStack> list = new ArrayList<>();

            for (Holder<Instrument> holder : BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey)) {
                ItemStack itemStack = InstrumentItem.create(instrument, holder);
                itemStack.setCount(1);
                list.add(itemStack);
            }
            entries.addBefore(comparedItem, list, tabVisibility);
        });
    }

    /**
     * Assists with adding each Firework Rocket duration to any Creative Mode tab.
     **/

    public static void addFireworkRockets(Item comparedItem, ResourceKey<CreativeModeTab> creativeModeTab) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register((entries) -> {
            for (byte fireworkDurations : FireworkRocketItem.CRAFTABLE_DURATIONS) {
                ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET);
                itemStack.set(DataComponents.FIREWORKS, new Fireworks(fireworkDurations, List.of()));
                entries.addBefore(comparedItem, itemStack);
            }
        });
    }

    /**
     * Assists with adding each Ominous Bottle variant to any Creative Mode tab.
     **/

    private static void addOminousBottles(int maxAmplifierValue, Item comparedItem, ResourceKey<CreativeModeTab> creativeModeTab) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register((entries) -> {
            for (int amplifiers = 0; amplifiers <= maxAmplifierValue; ++amplifiers) {
                ItemStack itemStack = new ItemStack(Items.OMINOUS_BOTTLE);
                itemStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, amplifiers);
                entries.addBefore(comparedItem, itemStack);
            }
        });
    }
}