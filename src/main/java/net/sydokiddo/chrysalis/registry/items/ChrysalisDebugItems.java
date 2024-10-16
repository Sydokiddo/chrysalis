package net.sydokiddo.chrysalis.registry.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.*;

public class ChrysalisDebugItems {

    // region Debug Items

    public static final Item HEAL = registerItem("heal", new HealItem(debugUtilitySettings()));
    public static final Item FILL_HUNGER = registerItem("fill_hunger", new FillHungerItem(debugUtilitySettings()));
    public static final Item GIVE_RESISTANCE = registerItem("give_resistance", new GiveResistanceItem(debugUtilitySettings()));
    public static final Item CLEAR_EFFECTS = registerItem("clear_effects", new ClearEffectsItem(debugUtilitySettings()));
    public static final Item TELEPORT_TO_SPAWNPOINT = registerItem("teleport_to_spawnpoint", new TeleportToSpawnpointItem(debugUtilitySettings()));
    public static final Item TELEPORT_WAND = registerItem("teleport_wand", new TeleportWandItem(debugUtilitySettings()));
    public static final Item KILL_WAND = registerItem("kill_wand", new KillWandItem(debugUtilitySettings().attributes(KillWandItem.createAttributes())));
    public static final Item TAME_MOB = registerItem("tame_mob", new TameMobItem(debugUtilitySettings()));
    public static final Item RIDE_MOB = registerItem("ride_mob", new RideMobItem(debugUtilitySettings()));

    // endregion

    // Registry

    private static Item.Properties debugUtilitySettings() {
        return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Chrysalis.id(name), item);
    }

    public static void registerDebugItems() {}
}