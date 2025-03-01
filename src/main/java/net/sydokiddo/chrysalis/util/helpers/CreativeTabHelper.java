package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.level.ItemLike;
import java.util.List;

@SuppressWarnings("unused")
public class CreativeTabHelper {

    /**
     * Assists with adding instrument items and their variants to any creative mode tab.
     **/

    public static void addInstrumentItem(ItemLike comparedItem, Item instrument, TagKey<Instrument> tagKey, HolderLookup<Instrument> holderLookup, ResourceKey<CreativeModeTab> creativeModeTab) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register((entries) ->
        holderLookup.get(tagKey).ifPresent((named) -> named.stream().map((holder) ->
        InstrumentItem.create(instrument, holder)).forEach((itemStack) -> entries.addBefore(comparedItem, itemStack))));
    }

    /**
     * Assists with adding each firework rocket duration to any creative mode tab.
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
     * Assists with adding each ominous bottle variant to any creative mode tab.
     **/

    private static void addOminousBottles(int maxAmplifierValue, Item comparedItem, ResourceKey<CreativeModeTab> creativeModeTab) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register((entries) -> {
            for (int amplifiers = 0; amplifiers <= maxAmplifierValue; ++amplifiers) {
                ItemStack itemStack = new ItemStack(Items.OMINOUS_BOTTLE);
                itemStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(amplifiers));
                entries.addBefore(comparedItem, itemStack);
            }
        });
    }
}