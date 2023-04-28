package net.sydokiddo.chrysalis.registry.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.custom_items.*;

@SuppressWarnings("ALL")
public class DebugItems {

    // List of Debug Items:

    public static final Item HEAL = registerItem("heal",
        new HealItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item FILL_HUNGER = registerItem("fill_hunger",
        new FillHungerItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item SET_SPAWNPOINT = registerItem("set_spawnpoint",
        new SetSpawnPointItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item TELEPORT_TO_SPAWNPOINT = registerItem("teleport_to_spawnpoint",
        new TeleportToSpawnpointItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Item KILL_WAND = registerItem("kill_wand",
        new KillWandItem(new FabricItemSettings().stacksTo(1).rarity(Rarity.EPIC)));

    // Registry for Items:

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Chrysalis.MOD_ID, name), item);
    }

    public static void registerDebugItems() {}
}
