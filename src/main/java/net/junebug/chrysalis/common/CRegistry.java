package net.junebug.chrysalis.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.junebug.chrysalis.client.entities.rendering.custom_entities.*;
import net.junebug.chrysalis.common.blocks.CBlocks;
import net.junebug.chrysalis.common.entities.registry.CBlockEntities;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.entities.custom_entities.spawners.entity_spawner.EntitySpawnerData;
import net.junebug.chrysalis.common.items.CDataComponents;
import net.junebug.chrysalis.util.blocks.codecs.BlockConversionData;
import net.junebug.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.junebug.chrysalis.util.entities.codecs.ChargedMobDropData;
import net.junebug.chrysalis.util.entities.codecs.PlayerLootTableData;
import net.junebug.chrysalis.util.helpers.DebugHelper;
import net.junebug.chrysalis.util.helpers.RegistryHelper;
import net.junebug.chrysalis.util.sounds.codecs.BlockSoundData;
import net.junebug.chrysalis.util.entities.CMemoryModules;
import net.junebug.chrysalis.common.entities.registry.CEntities;
import net.junebug.chrysalis.common.items.CItems;
import net.junebug.chrysalis.common.misc.*;
import net.junebug.chrysalis.common.status_effects.CStatusEffects;
import org.lwjgl.glfw.GLFW;
import java.awt.*;
import java.util.*;

public class CRegistry {

    /**
     * The base registry for chrysalis, which initializes all other registries.
     **/

    // region Entity Data

    public static final EntityDataAccessor<Integer>
        ITEM_GLOW_COLOR = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT)
    ;

    public static final EntityDataAccessor<Optional<UUID>>
        ENCOUNTERED_MOB_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID)
    ;

    // endregion

    // region Custom Music

    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();
    public record StructureMusicSound(Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {}

    // endregion

    // region Data Driven Features

    public static final ResourceKey<Registry<BlockPropertyData>> BLOCK_PROPERTY_DATA = RegistryHelper.registerBlockDataType("properties");
    public static final ResourceKey<Registry<BlockSoundData>> BLOCK_SOUND_DATA = RegistryHelper.registerBlockDataType("sound_group");
    public static final ResourceKey<Registry<BlockConversionData>> BLOCK_CONVERSION_DATA = RegistryHelper.registerBlockDataType("conversion");
    public static final ResourceKey<Registry<ChargedMobDropData>> CHARGED_MOB_DROP_DATA = RegistryHelper.registerEntityDataType("charged_mob_drop");
    public static final ResourceKey<Registry<PlayerLootTableData>> PLAYER_LOOT_TABLE_DATA = RegistryHelper.registerEntityDataType("player_loot_table");
    public static final ResourceKey<Registry<EntitySpawnerData.EntitySpawnerConfig>> ENTITY_SPAWNER_CONFIG_DATA = RegistryHelper.registerEntityDataType("entity_spawner_config");

    public static boolean isDataEnabled(String enabled) {

        boolean returnValue = false;

        switch (enabled) {
            case "true" -> returnValue = true;
            case "false" -> {}
            case "debug_environment_only" -> { if (Chrysalis.IS_DEBUG) returnValue = true; }
            default -> throw new IllegalArgumentException("Invalid data enabled state '" + enabled + "', must be either 'true', 'false', or 'debug_environment_only'");
        }

        return returnValue;
    }

    // endregion

    public static void registerAll(IEventBus eventBus) {

        if (Chrysalis.IS_DEBUG) {
            if (Chrysalis.registerTestItems) CItems.registerTestItems(eventBus);
            if (Chrysalis.registerExampleRegistry) CExampleRegistry.init(eventBus);
        }

        CItems.register(eventBus);
        CBlocks.register(eventBus);
        CBlockEntities.register(eventBus);
        CDataComponents.register(eventBus);
        CCreativeModeTabs.register(eventBus);
        CSoundEvents.register(eventBus);
        CGameEvents.register(eventBus);
        CParticles.register(eventBus);
        CEntities.register(eventBus);
        CMemoryModules.register(eventBus);
        CDamageTypes.register();
        CAttributes.register(eventBus);
        CStatusEffects.register(eventBus);
        CGameRules.register();
        CCriteriaTriggers.register(eventBus);
    }

    @SuppressWarnings("unused")
    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientRegistry {

        public static int devCapeVariant;
        public static final KeyMapping PANORAMIC_SCREENSHOT_KEY = new KeyMapping("key.chrysalis.panoramic_screenshot", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, "key.categories.misc");

        @SubscribeEvent
        private static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(CEntities.SEAT.get(), SeatRenderer::new);
            EntityRenderers.register(CEntities.ENTITY_SPAWNER.get(), EntitySpawnerRenderer::new);
            EntityRenderers.register(CEntities.ENCOUNTER_SPAWNER.get(), EncounterSpawnerRenderer::new);
            EntityRenderers.register(CEntities.EARTHQUAKE.get(), NoopRenderer::new);
            BlockEntityRenderers.register(CBlockEntities.BARRICADE_BLOCK.get(), BarricadeBlockRenderer::new);
            BlockEntityRenderers.register(CBlockEntities.PLACEHOLDER_BLOCK.get(), PlaceholderBlockRenderer::new);

            try {
                Toolkit.getDefaultToolkit().getSystemClipboard();
            } catch (HeadlessException exception) {
                Chrysalis.LOGGER.warn("java.awt.headless property was not set properly!");
            }

            devCapeVariant = (Math.random() <= 0.5D) ? 1 : 2;
            DebugHelper.sendInitializedMessage(Chrysalis.LOGGER, Chrysalis.MOD_VERSION, true);
        }
    }
}