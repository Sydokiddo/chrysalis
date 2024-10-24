package net.sydokiddo.chrysalis.registry.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.*;
import java.util.function.Function;

public class ChrysalisDebugItems {

    // region Debug Items

    public static final Item HEAL = registerItem("heal", HealItem::new, debugUtilitySettings());
    public static final Item FILL_HUNGER = registerItem("fill_hunger", FillHungerItem::new, debugUtilitySettings());
    public static final Item GIVE_RESISTANCE = registerItem("give_resistance", GiveResistanceItem::new, debugUtilitySettings());
    public static final Item CLEAR_EFFECTS = registerItem("clear_effects", ClearEffectsItem::new, debugUtilitySettings());
    public static final Item TELEPORT_TO_SPAWNPOINT = registerItem("teleport_to_spawnpoint", TeleportToSpawnpointItem::new, debugUtilitySettings());
    public static final Item TELEPORT_WAND = registerItem("teleport_wand", TeleportWandItem::new, debugUtilitySettings().useCooldown(3.0F));
    public static final Item KILL_WAND = registerItem("kill_wand", KillWandItem::new, debugUtilitySettings().attributes(KillWandItem.createAttributes()));
    public static final Item TAME_MOB = registerItem("tame_mob", TameMobItem::new, debugUtilitySettings());
    public static final Item RIDE_MOB = registerItem("ride_mob", RideMobItem::new, debugUtilitySettings());

    // endregion

    // Registry

    private static Item.Properties debugUtilitySettings() {
        return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC);
    }

    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        ResourceKey<Block> resourceKey = ResourceKey.create(Registries.BLOCK, Chrysalis.id(name));
        Block block = function.apply(properties.setId(resourceKey));
        registerItem(name, blockItemProperties -> new BlockItem(block, blockItemProperties), itemProperties);
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }

    public static Item registerItem(String name, Function<Item.Properties, Item> function, Item.Properties properties) {
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, Chrysalis.id(name));
        Item item = function.apply(properties.setId(resourceKey));
        if (item instanceof BlockItem blockItem) blockItem.registerBlocks(Item.BY_BLOCK, item);
        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    public static void registerDebugItems() {}
}