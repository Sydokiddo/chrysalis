package net.sydokiddo.chrysalis.registry.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.*;

@SuppressWarnings("ALL")
public class ChrysalisDebugItems {

    // List of Debug Items:

    public static final Item HEAL = registerItem("heal",
        new HealItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item FILL_HUNGER = registerItem("fill_hunger",
        new FillHungerItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item GIVE_RESISTANCE = registerItem("give_resistance",
        new GiveResistanceItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item CLEAR_EFFECTS = registerItem("clear_effects",
        new ClearEffectsItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item TELEPORT_TO_SPAWNPOINT = registerItem("teleport_to_spawnpoint",
        new TeleportToSpawnpointItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item KILL_WAND = registerItem("kill_wand",
        new KillWandItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    // Registry for Items:

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Chrysalis.id(name), item);
    }

    public static void registerDebugItems() {}
}