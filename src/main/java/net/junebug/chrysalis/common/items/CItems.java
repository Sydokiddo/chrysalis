package net.junebug.chrysalis.common.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.items.custom_items.IconItem;
import net.junebug.chrysalis.common.items.custom_items.CreativeModeDescriptionItem;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.ExtraReachDebugUtilityItem;
import net.junebug.chrysalis.common.items.custom_items.debug_items.types.*;
import net.junebug.chrysalis.common.items.custom_items.examples_and_testing.TestRightClickItem;
import net.junebug.chrysalis.util.helpers.RegistryHelper;

public class CItems {

    public static final DeferredRegister.Items
        ITEMS = DeferredRegister.createItems(Chrysalis.MOD_ID),
        TEST_ITEMS = DeferredRegister.createItems(Chrysalis.MOD_ID)
    ;

    // region Debug Items

    public static final DeferredItem<Item>
        ICON = ITEMS.registerItem("icon", IconItem::new, RegistryHelper.iconProperties()),
        HEAL = ITEMS.registerItem("heal", HealItem::new, RegistryHelper.debugUtilityProperties(1)),
        FILL_HUNGER = ITEMS.registerItem("fill_hunger", FillHungerItem::new, RegistryHelper.debugUtilityProperties(1)),
        FILL_OXYGEN = ITEMS.registerItem("fill_oxygen", FillOxygenItem::new, RegistryHelper.debugUtilityProperties(1)),
        GIVE_RESISTANCE = ITEMS.registerItem("give_resistance", GiveResistanceItem::new, RegistryHelper.debugUtilityProperties(1)),
        CLEAR_EFFECTS = ITEMS.registerItem("clear_effects", ClearEffectsItem::new, RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_TO_SPAWNPOINT = ITEMS.registerItem("teleport_to_spawnpoint", TeleportToSpawnpointItem::new, RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_WAND = ITEMS.registerItem("teleport_wand", TeleportWandItem::new, RegistryHelper.debugUtilityProperties(1).useCooldown(3.0F)),
        KILL_WAND = ITEMS.registerItem("kill_wand", KillWandItem::new, RegistryHelper.debugUtilityProperties(1).attributes(ExtraReachDebugUtilityItem.createAttributes(EquipmentSlotGroup.MAINHAND))),
        AGGRO_WAND = ITEMS.registerItem("aggro_wand", AggroWandItem::new, RegistryHelper.debugUtilityProperties(1).attributes(ExtraReachDebugUtilityItem.createAttributes(EquipmentSlotGroup.HAND))),
        TAME_MOB = ITEMS.registerItem("tame_mob", TameMobItem::new, RegistryHelper.debugUtilityProperties(1)),
        RIDE_MOB = ITEMS.registerItem("ride_mob", RideMobItem::new, RegistryHelper.debugUtilityProperties(1)),
        DRAIN_FLUIDS = ITEMS.registerItem("drain_fluids", DrainFluidsItem::new, RegistryHelper.debugUtilityProperties(1)),
        COPYING_SPAWN_EGG = ITEMS.registerItem("copying_spawn_egg", CopyingSpawnEggItem::new, RegistryHelper.debugUtilityProperties(1)),
        KEY = ITEMS.registerItem("key", CreativeModeDescriptionItem::new, new Item.Properties()),
        ADMIN_KEY = ITEMS.registerItem("admin_key", CreativeModeDescriptionItem::new, RegistryHelper.debugUtilityProperties(64)),
        TEST_RIGHT_CLICK_ITEM = TEST_ITEMS.registerItem("test_right_click_item", TestRightClickItem::new, new Item.Properties().stacksTo(1))
    ;

    public static final DeferredItem<BlockItem>
        NETHER_PORTAL = registerItemForVanillaBlock("nether_portal", Blocks.NETHER_PORTAL),
        END_PORTAL = registerItemForVanillaBlock("end_portal", Blocks.END_PORTAL),
        END_GATEWAY = registerItemForVanillaBlock("end_gateway", Blocks.END_GATEWAY)
    ;

    // endregion

    // region Registry

    private static DeferredItem<BlockItem> registerItemForVanillaBlock(String name, Block block) {
        return ITEMS.register(name, (key) -> new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, key)).overrideDescription(block.getDescriptionId())));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static void registerTestItems(IEventBus eventBus) {
        TEST_ITEMS.register(eventBus);
    }

    // endregion
}