package net.junebug.chrysalis.common.items;

import net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake.Earthquake;
import net.junebug.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawner;
import net.junebug.chrysalis.util.helpers.EventHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
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
import org.jetbrains.annotations.NotNull;

public class CItems {

    public static final DeferredRegister.Items
        ITEMS = DeferredRegister.createItems(Chrysalis.MOD_ID),
        TEST_ITEMS = DeferredRegister.createItems(Chrysalis.MOD_ID)
    ;

    // region Items

    public static final DeferredItem<Item>
        ICON = ITEMS.registerItem("icon", IconItem::new, RegistryHelper.iconProperties()),
        HEAL = ITEMS.registerItem("heal", HealItem::new, RegistryHelper.debugUtilityProperties(1)),
        FILL_HUNGER = ITEMS.registerItem("fill_hunger", FillHungerItem::new, RegistryHelper.debugUtilityProperties(1)),
        FILL_OXYGEN = ITEMS.registerItem("fill_oxygen", FillOxygenItem::new, RegistryHelper.debugUtilityProperties(1)),
        GIVE_RESISTANCE = ITEMS.registerItem("give_resistance", GiveResistanceItem::new, RegistryHelper.debugUtilityProperties(1)),
        CLEAR_EFFECTS = ITEMS.registerItem("clear_effects", ClearEffectsItem::new, RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_TO_SPAWNPOINT = ITEMS.registerItem("teleport_to_spawnpoint", TeleportToSpawnpointItem::new, RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_TO_OVERWORLD = ITEMS.registerItem("teleport_to_overworld", (properties) -> new TeleportToDimensionItem(properties, Level.OVERWORLD), RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_TO_NETHER = ITEMS.registerItem("teleport_to_nether", (properties) -> new TeleportToDimensionItem(properties, Level.NETHER), RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_TO_END = ITEMS.registerItem("teleport_to_end", (properties) -> new TeleportToDimensionItem(properties, Level.END), RegistryHelper.debugUtilityProperties(1)),
        TELEPORT_WAND = ITEMS.registerItem("teleport_wand", TeleportWandItem::new, RegistryHelper.debugUtilityProperties(1).useCooldown(3.0F)),
        KILL_WAND = ITEMS.registerItem("kill_wand", KillWandItem::new, RegistryHelper.debugUtilityProperties(1).attributes(ExtraReachDebugUtilityItem.createAttributes(EquipmentSlotGroup.MAINHAND))),
        AGGRO_WAND = ITEMS.registerItem("aggro_wand", AggroWandItem::new, RegistryHelper.debugUtilityProperties(1).attributes(ExtraReachDebugUtilityItem.createAttributes(EquipmentSlotGroup.HAND))),
        TAME_MOB = ITEMS.registerItem("tame_mob", TameMobItem::new, RegistryHelper.debugUtilityProperties(1)),
        RIDE_MOB = ITEMS.registerItem("ride_mob", RideMobItem::new, RegistryHelper.debugUtilityProperties(1)),
        DRAIN_FLUIDS = ITEMS.registerItem("drain_fluids", DrainFluidsItem::new, RegistryHelper.debugUtilityProperties(1)),
        COPYING_SPAWN_EGG = ITEMS.registerItem("copying_spawn_egg", CopyingSpawnEggItem::new, RegistryHelper.debugUtilityProperties(1)),
        KEY = ITEMS.registerItem("key", CreativeModeDescriptionItem::new, new Item.Properties()),
        ADMIN_KEY = ITEMS.registerItem("admin_key", CreativeModeDescriptionItem::new, RegistryHelper.debugUtilityProperties(64)),

        MUSIC_EVENT_TEST = ITEMS.registerItem("music_event_test", (properties) -> new EventTestItem("music_event_test") {

            @Override
            public boolean emitEvent(Level level, Player player, @NotNull InteractionHand interactionHand) {

                if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    EventHelper.sendMusic(serverPlayer, SoundEvents.MUSIC_MENU, 10, 10, true);
                    return true;
                }

                return super.emitEvent(level, player, interactionHand);
            }
        }),

        CAMERA_SHAKE_EVENT_TEST = ITEMS.registerItem("camera_shake_event_test", (properties) -> new EventTestItem("camera_shake_event_test") {

            @Override
            public boolean emitEvent(Level level, Player player, @NotNull InteractionHand interactionHand) {

                if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    EventHelper.sendCameraShake(serverPlayer, 100, 5, 5);
                    return true;
                }

                return super.emitEvent(level, player, interactionHand);
            }
        }),

        EARTHQUAKE_EVENT_TEST = ITEMS.registerItem("earthquake_event_test", (properties) -> new EventTestItem("earthquake_event_test") {

            @Override
            public boolean emitEvent(Level level, Player player, @NotNull InteractionHand interactionHand) {

                if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    Earthquake.create(level, serverPlayer, player.getOnPos().above().getBottomCenter(), player.getYRot(), player.getXRot());
                    return true;
                }

                return super.emitEvent(level, player, interactionHand);
            }
        }),

        ENTITY_SPAWNER_EVENT_TEST = ITEMS.registerItem("entity_spawner_event_test", (properties) -> new EventTestItem("entity_spawner_event_test") {

            @Override
            public boolean emitEvent(Level level, Player player, @NotNull InteractionHand interactionHand) {

                if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    EntitySpawner.create(level, Chrysalis.stringId("example"), serverPlayer.position());
                    return true;
                }

                return super.emitEvent(level, player, interactionHand);
            }
        }),

        GIANT_SPAWN_EGG = ITEMS.registerItem("giant_spawn_egg", (properties) -> new SpawnEggItem(EntityType.GIANT, properties)),
        ILLUSIONER_SPAWN_EGG = ITEMS.registerItem("illusioner_spawn_egg", (properties) -> new SpawnEggItem(EntityType.ILLUSIONER, properties)),

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