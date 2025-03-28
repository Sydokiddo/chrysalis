package net.sydokiddo.chrysalis.common.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.custom_items.IconItem;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.*;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.base_classes.ExtraReachDebugUtilityItem;
import net.sydokiddo.chrysalis.common.items.custom_items.examples_and_testing.TestRightClickItem;

public class ChrysalisItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chrysalis.MOD_ID);
    private static final DeferredRegister.Items TEST_ITEMS = DeferredRegister.createItems(Chrysalis.MOD_ID);

    // region Debug Items

    public static final DeferredItem<Item>
        ICON = ITEMS.registerItem("icon", IconItem::new, iconSettings()),
        HEAL = ITEMS.registerItem("heal", HealItem::new, debugUtilitySettings()),
        FILL_HUNGER = ITEMS.registerItem("fill_hunger", FillHungerItem::new, debugUtilitySettings()),
        FILL_OXYGEN = ITEMS.registerItem("fill_oxygen", FillOxygenItem::new, debugUtilitySettings()),
        GIVE_RESISTANCE = ITEMS.registerItem("give_resistance", GiveResistanceItem::new, debugUtilitySettings()),
        CLEAR_EFFECTS = ITEMS.registerItem("clear_effects", ClearEffectsItem::new, debugUtilitySettings()),
        TELEPORT_TO_SPAWNPOINT = ITEMS.registerItem("teleport_to_spawnpoint", TeleportToSpawnpointItem::new, debugUtilitySettings()),
        TELEPORT_WAND = ITEMS.registerItem("teleport_wand", TeleportWandItem::new, debugUtilitySettings().useCooldown(3.0F)),
        KILL_WAND = ITEMS.registerItem("kill_wand", KillWandItem::new, debugUtilitySettings().attributes(ExtraReachDebugUtilityItem.createAttributes(EquipmentSlotGroup.MAINHAND))),
        AGGRO_WAND = ITEMS.registerItem("aggro_wand", AggroWandItem::new, debugUtilitySettings().attributes(ExtraReachDebugUtilityItem.createAttributes(EquipmentSlotGroup.HAND))),
        TAME_MOB = ITEMS.registerItem("tame_mob", TameMobItem::new, debugUtilitySettings()),
        RIDE_MOB = ITEMS.registerItem("ride_mob", RideMobItem::new, debugUtilitySettings()),
        COPYING_SPAWN_EGG = ITEMS.registerItem("copying_spawn_egg", CopyingSpawnEggItem::new, debugUtilitySettings()),
        TEST_RIGHT_CLICK_ITEM = ChrysalisItems.TEST_ITEMS.registerItem("test_right_click_item", TestRightClickItem::new, new Item.Properties().stacksTo(1))
    ;

    // endregion

    // region Registry

    public static Item.Properties debugUtilitySettings() {
        return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    public static Item.Properties iconSettings() {
        return new Item.Properties().stacksTo(1).rarity(Rarity.EPIC);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static void registerTestItems(IEventBus eventBus) {
        TEST_ITEMS.register(eventBus);
    }

    // endregion
}